package com.springboot.blog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {

    private Long id;

    //title should not be empty
    //should have 2 char at least
    @NotEmpty
    @Size(min = 2 , message = "title should have 2 char at least")
    private String title;


    //Description should not be empty
    //should have 10 char at least
    @NotEmpty
    @Size(min = 10 , message = "title should have 10 char at least")
    private String description;

    //Post Content should not be empty
    @NotEmpty
    private String content;
    private List<CommentDto>comments;


}
