package com.springboot.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;

    //name should not be empty
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    //Email should not be empty
    @NotEmpty(message = "email not be empty")
    @Email
    private String email;

    @Size(min = 10 , message = "Comment must be at least 10 characters")
    private String body;
}
