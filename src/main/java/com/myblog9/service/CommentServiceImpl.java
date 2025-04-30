package com.myblog9.service;

import com.myblog9.entity.Comments;
import com.myblog9.entity.Post;
import com.myblog9.exception.ResourceNotFound;
import com.myblog9.payload.CommentDto;
import com.myblog9.payload.CommentResponse;
import com.myblog9.repository.CommentRepository;
import com.myblog9.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{


    private CommentRepository commentrepo;
    private PostRepository postrepo;
    private ModelMapper modelmapper;
    public CommentServiceImpl(CommentRepository commentrepo,PostRepository postrepo,
                              ModelMapper modelmapper) {
        this.commentrepo = commentrepo;
        this.postrepo = postrepo;
        this.modelmapper=modelmapper;
    }


    @Override
    public CommentDto saveComment(long postid, CommentDto dto) {

        Post post = postrepo.findById(postid).orElseThrow(
                ()-> new ResourceNotFound("Post not found with id:"+postid)
        );

        Comments comment = new Comments();
        //comment.setId(dto.getId());
        comment.setName(dto.getName());
        comment.setEmail(dto.getEmail());
        comment.setBody(dto.getBody());
        comment.setPost(post);
        Comments savedComment = commentrepo.save(comment);

        CommentDto saveddto = mapToDto(savedComment);
        return saveddto;
    }

    @Override
    public void deleteCommentById(long id) {
        commentrepo.deleteById(id);
    }

    @Override
    public CommentDto updateComment(long id, CommentDto dto) {

        Comments comment = commentrepo.findById(id).orElseThrow(
                ()-> new ResourceNotFound("Comment not found with id:"+id)
        );
        comment.setName(dto.getName());
        comment.setEmail(dto.getEmail());
        comment.setBody(dto.getBody());
        Comments updatedcomment = commentrepo.save(comment);
        return mapToDto(updatedcomment);
    }

    @Override
    public CommentDto getCommentById(long id) {
        Optional<Comments> findbyid = commentrepo.findById(id);
        if(findbyid.isPresent()){
            Comments comment = findbyid.get();
            return mapToDto(comment);
        }
        return null;
    }

    @Override
    public CommentResponse getAllComments(
            int pageno, int pagesize, String sortby, String sortdir) {
        Sort sort = sortdir.equalsIgnoreCase(
                Sort.Direction.ASC.name())?Sort.by(sortby).ascending():Sort.by(sortby).descending();
        Pageable pageable = PageRequest.of(pageno,pagesize,sort);
        Page<Comments> all = commentrepo.findAll(pageable);
        List<Comments> comments = all.getContent();
        List<CommentDto> alldto = comments.stream().map(c->mapToDto(c)).collect(Collectors.toList());
        CommentResponse response = new CommentResponse();

       response.setContent(alldto);
       response.setPageno(all.getNumber());
       response.setPagesize(all.getSize());
       response.setTotalelement((int)all.getTotalElements());
       response.setTotalpages(all.getTotalPages());
       response.setLast(all.isLast());
       return response;
    }

    CommentDto mapToDto(Comments comment){

        CommentDto dto = modelmapper.map(comment,CommentDto.class);
//        CommentDto dto = new CommentDto();
//        dto.setId(comment.getId());
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setBody(comment.getBody());
        return dto;
    }
}
