package com.myblog9.controller;

import com.myblog9.entity.Post;
import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;
import com.myblog9.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {


    private PostService postservice;

    public PostController(PostService postservice) {
        this.postservice = postservice;
    }

    //http://localhost:8080/api/posts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postdto,
                                              BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = postservice.createPost(postdto);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts/{postid}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{postid}")
    public ResponseEntity<String> deletePostById(@PathVariable("postid") long id){

        postservice.deleteById(id);
        return new ResponseEntity<>("Post is deleted" +id,HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/{postid}
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{postid}")
    public ResponseEntity<Object> updatepost(@PathVariable("postid") long id, @Valid@RequestBody PostDto postdto,
                                              BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
       PostDto updateddto = postservice.updatepost(id,postdto);
       return new ResponseEntity<>(updateddto,HttpStatus.OK);
    }
    //http://localhost:8080/api/posts?pageno=1&pagesize=4&sortby=title&sortdir=ASC/DESC
    @GetMapping
    public PostResponse listPost(
            @RequestParam(value="pageno",defaultValue ="0",required = false) int pageno,
            @RequestParam(value="pagesize",defaultValue = "4",required = false) int pagesize,
            @RequestParam(value="sortby",defaultValue="id",required=false) String sortby,
            @RequestParam(value="sortdir",defaultValue = "asc",required = false) String sortdir){
        PostResponse postresponse = postservice.listAll(pageno,pagesize,sortby,sortdir);
       return postresponse;

    }
    //http://localhost:8080/api/posts/{postid}
    @GetMapping("{postid}")
    public PostDto getPostById(@PathVariable("postid") long id){
        PostDto dto = postservice.getPostById(id);
        return dto;
    }
}
