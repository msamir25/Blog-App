package com.springboot.blog.controller;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstrains;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    //inject Post service to the controller class
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create  endPoints to the crearing post

    @Operation(summary =  "Create Post REST API")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto>createPost(@Valid @RequestBody PostDto postDto){

     return new ResponseEntity<>(postService.createPost(postDto) , HttpStatus.CREATED);

    }

    //create a end point to get all posts
    @Operation(summary =  "get all Posts REST API")

    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts(
            @RequestParam(name = "pageNo" , defaultValue = AppConstrains.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize" ,defaultValue = AppConstrains.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy" , defaultValue = AppConstrains.DEFAULT_SORT_BY , required = false) String sortBy,
            @RequestParam(name = "sortDir" , defaultValue = AppConstrains.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo , pageSize , sortBy , sortDir);
    }
    @Operation(summary = "get Post REST API by Id")

    @GetMapping(value = "/api/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id){
            return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary =  "Update Post REST API")


    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> update(@Valid @RequestBody PostDto postDto
            , @PathVariable(name = "id") Long id){
        PostDto postDto1 = postService.updatePostById(postDto, id);
        return new ResponseEntity<>(postDto1 , HttpStatus.OK);
    }
    @Operation(summary =  "Delete Post REST API")

    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(name ="id") Long id){
        postService.deleteById(id);

        return new ResponseEntity<>("Post is deleted successfully" , HttpStatus.OK);
    }

}
