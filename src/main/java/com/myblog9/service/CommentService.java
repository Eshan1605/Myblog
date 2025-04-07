package com.myblog9.service;

import com.myblog9.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto saveComment(long postid, CommentDto commentdto);

    void deleteCommentById(long id);

    CommentDto updateComment(long id, CommentDto dto);

    CommentDto getCommentById(long id);

    List<CommentDto> getAllComments();
}
