package com.springboot.blog.service.serviceImpl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper modelMapper;


    public CommentServiceImpl(CommentRepository commentRepository , PostRepository postRepository , ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;

    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
            //Convert DTO to Entity
            Comment comment = mapToEntity(commentDto);

            //Find the post we create the comment in
            Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
            );

            //Set Post to Comment Entity
            comment.setPost(post);

            //Save Comment to DB
            Comment savedComment = commentRepository.save(comment);

            return mapToDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(Long postId) {
        //Retrive comment by post id
        List<Comment> comments = commentRepository.findByPostId(postId);

        //Return a list of comments
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        //Find the post we create the comment in
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment" , "Comment id " , commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST , "Comment doesnot belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentrequest) {
        //Find the post we create the comment in
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );
        //Get Comment From Database
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment" , "Comment id " , commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST , "Comment doesnot belong to post");
        }

        //get the updated paramters from the Api
        comment.setName(commentrequest.getName());
        comment.setBody(commentrequest.getBody());
        comment.setEmail(commentrequest.getEmail());

        //Save it to DB

        Comment savedcomment = commentRepository.save(comment);

        return mapToDto(savedcomment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        //Find the post we create the comment in
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );
        //Get Comment From Database
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment" , "Comment id " , commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST , "Comment doesnot belong to post");
        }

        commentRepository.delete(comment);

    }

    private CommentDto mapToDto(Comment comment){
         CommentDto commentDto = modelMapper.map(comment , CommentDto.class);
/*       CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());*/
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment =modelMapper.map(commentDto,Comment.class);
/*        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());*/
        return comment;
    }


}
