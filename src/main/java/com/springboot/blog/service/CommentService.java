package com.springboot.blog.service;

import com.springboot.blog.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long PostId , CommentDto commentDto);

    List<CommentDto> getCommentByPostId(Long postId);

    CommentDto getCommentById(Long postId , Long commentId);


    CommentDto updateComment(Long postId , Long commentId , CommentDto commentDto);

    void deleteComment(Long postId , Long commentId);
}
