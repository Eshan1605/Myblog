package com.myblog9.service;

import com.myblog9.entity.Comments;
import com.myblog9.entity.Post;
import com.myblog9.exception.ResourceNotFound;
import com.myblog9.payload.CommentDto;
import com.myblog9.repository.CommentRepository;
import com.myblog9.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public List<CommentDto> getAllComments() {
        List<Comments> comments = commentrepo.findAll();
       List<CommentDto> alldto = comments.stream().map(c->mapToDto(c)).collect(Collectors.toList());
        return alldto;
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
