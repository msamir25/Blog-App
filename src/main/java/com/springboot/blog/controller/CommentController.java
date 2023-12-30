package com.springboot.blog.controller;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

    //inject the service
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @Operation(summary =  "Create a comment to Posts REST API")

    @PostMapping({"/posts/{postId}/comments"})
    public ResponseEntity<CommentDto>createComment(@PathVariable(value = "postId") Long postId ,
                                                   @Valid @RequestBody CommentDto commentDto){

        return new ResponseEntity<>(commentService.createComment(postId , commentDto) , HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getAllCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return commentService.getCommentByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
                                                     @PathVariable(value = "commentId") Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId, commentId);

        return new ResponseEntity<>(commentDto , HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId ,
                                                    @PathVariable(value = "commentId") Long commentId ,
                                                    @Valid @RequestBody CommentDto commentRequested){
        CommentDto commentDto = commentService.updateComment(postId, commentId, commentRequested);

        return new ResponseEntity<>(commentDto , HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String>deleteComment(@PathVariable(value = "postId") Long postId ,
                                               @PathVariable(value = "commentId") Long commentId){
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment Deleted Successfully " , HttpStatus.OK);

    }
}
