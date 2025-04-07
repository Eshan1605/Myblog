package com.myblog9.service;

import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;

import java.util.List;

public interface PostService {

    public PostDto createPost(PostDto postdto);

    void deleteById(long id);

    PostDto updatepost(Long id ,PostDto postdto);

    PostResponse listAll(int pageno, int pagesize, String sortby, String sortdir);

    PostDto getPostById(long id);
}

