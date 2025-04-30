package com.myblog9.service;

import com.myblog9.payload.CommentDto;
import com.myblog9.payload.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentDto saveComment(long postid, CommentDto commentdto);

    void deleteCommentById(long id);

    CommentDto updateComment(long id, CommentDto dto);

    CommentDto getCommentById(long id);

    CommentResponse getAllComments(int pageno, int pagesize, String sortby, String sortdir);
}
