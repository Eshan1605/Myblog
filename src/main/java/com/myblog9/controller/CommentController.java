package com.myblog9.controller;

import com.myblog9.payload.CommentDto;
import com.myblog9.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private CommentService commentservice;

    public CommentController(CommentService commentservice) {
        this.commentservice = commentservice;
    }

    //http://localhost:8080/api/comments/{postid}
    @PostMapping("{postid}")
    public ResponseEntity<CommentDto> saveComment
    (@PathVariable long postid,@RequestBody CommentDto commentdto){

        CommentDto saveddto = commentservice.saveComment(postid,commentdto);
        return new ResponseEntity<>(saveddto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/comments/{id}
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("id") long id){

        commentservice.deleteCommentById(id);
        return new ResponseEntity<>("Comment is deleted",HttpStatus.OK);
    }

    //http://localhost:8080/api/comments/{id}
    @PutMapping("{id}")
    public ResponseEntity<CommentDto> updateComment
    (@PathVariable("id") long id,@RequestBody CommentDto dto){
        CommentDto updatedDto =commentservice.updateComment(id,dto);
        return new ResponseEntity<>(updatedDto,HttpStatus.OK);
    }

    //http://localhost:8080/api/comments/{id}
    @GetMapping("{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable long id){
       CommentDto getdto = commentservice.getCommentById(id);
       return new ResponseEntity<>(getdto,HttpStatus.OK);
    }

    //http://localhost:8080/api/comments
    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments(){
        List<CommentDto> alldtos = commentservice.getAllComments();
        return new ResponseEntity<>(alldtos,HttpStatus.OK);
    }
}
