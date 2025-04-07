package com.myblog9.service.impl;

import com.myblog9.entity.Post;
import com.myblog9.exception.ResourceNotFound;
import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;
import com.myblog9.repository.PostRepository;
import com.myblog9.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postrepository;

    private ModelMapper modelmapper;

    public PostServiceImpl(PostRepository postrepository,ModelMapper modelmapper) {

        this.postrepository = postrepository;
        this.modelmapper=modelmapper;
    }


    @Override
    public PostDto createPost(PostDto postdto) {

        Post post = new Post();
        post.setTitle(postdto.getTitle());
        post.setDescription(postdto.getDescription());
        post.setContent(postdto.getContent());

        Post saveddpost = postrepository.save(post);

        PostDto dto = new PostDto();
        dto.setId(saveddpost.getId());
        dto.setTitle(saveddpost.getTitle());
        dto.setDescription(saveddpost.getDescription());
        dto.setContent(saveddpost.getContent());

        return dto;
    }

    @Override
    public void deleteById(long id) {
        Post post = postrepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound(" post not found with id "+id)
        );
        postrepository.deleteById(id);
    }

    @Override
    public PostDto updatepost(Long id ,PostDto postdto) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(postdto.getTitle());
        post.setDescription(postdto.getDescription());
        post.setContent(postdto.getContent());

        Post updateddpost = postrepository.save(post);

        return mapToDto(updateddpost);

    }

    @Override
    public PostResponse listAll(int pageno, int pagesize, String sortby, String sortdir) {
        Sort sort =sortdir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortby).ascending():Sort.by(sortby).descending();
        Pageable pageable = PageRequest.of(pageno,pagesize,sort);
        Page<Post> all  =postrepository.findAll(pageable);
        List<Post>posts = all.getContent();
        List<PostDto> dtos =posts.stream().map(post->mapToDto(post)).collect(Collectors.toList());

        PostResponse postresponse = new PostResponse();
        postresponse.setContent(dtos);
        postresponse.setPageno(all.getNumber());
        postresponse.setPagesize(all.getSize());
        postresponse.setTotalelement((int)all.getTotalElements());
        postresponse.setTotalpages(all.getTotalPages());
        postresponse.setLast(all.isLast());
        return postresponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postrepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound("post with post id:"+id +"not found")
        );
        return mapToDto(post);
    }

    PostDto mapToDto(Post post){
        PostDto dto = modelmapper.map(post,PostDto.class);
        //PostDto dto = new PostDto();
        //dto.setId(post.getId());
        //dto.setTitle(post.getTitle());
        //dto.setContent(post.getContent());
        //dto.setDescription(post.getDescription());
        return dto;
    }
}
