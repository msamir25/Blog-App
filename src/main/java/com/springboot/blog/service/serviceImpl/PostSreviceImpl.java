package com.springboot.blog.service.serviceImpl;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostSreviceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper modelMapper;


    @Autowired
    public PostSreviceImpl(PostRepository postRepository  , ModelMapper modelMapper){
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public PostDto createPost(PostDto postDto) {

        //Convert Dto to Entity
        Post post = mapToEntity(postDto);

        Post newPost = postRepository.save(post);

        //Convert Entity to DTO

        PostDto postResponse = mapToDto(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo , int pageSize ,String sortBy ,String sortDir) {


        //create pageable instance

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo , pageSize , sort);

       Page<Post> allPosts = postRepository.findAll(pageable);

        List<Post>listOfPosts = allPosts.getContent();
        //Convert Entity to DTO
        List<PostDto> content= listOfPosts
                .stream().map(post -> mapToDto(post))
                .collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();

        postResponse.setContent(content);
        postResponse.setPageNo(allPosts.getNumber());
        postResponse.setContent(content);
        postResponse.setPageSize(allPosts.getSize());
        postResponse.setTotalPages(allPosts.getTotalPages());
        postResponse.setTotalElements(allPosts.getTotalElements());
        postResponse.setLast(allPosts.isLast());
        return postResponse;

    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("post" , "id" ,id));

//        ListPostDto>collected;
//        if(post.isPresent()){
//             collected = post.stream()
//            .map(post1 -> mapToDto(post1))
//            .collect(Collectors.toList());
//
//        }


        return mapToDto(post);
    }

    @Override
    public PostDto updatePostById(PostDto postDto, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("post" , "id" ,id));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        return mapToDto(post);
    }

    @Override
    public void deleteById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("post" , "id" ,id));
        postRepository.delete(post);
    }

    //convert Dto to Entity
    private Post mapToEntity(PostDto postDto){
       Post post = modelMapper.map(postDto , Post.class);
/*        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());*/
        return post;
    }

    //Convert Entity To Dto
    private PostDto mapToDto(Post newPost){
        PostDto postResponse = modelMapper.map(newPost , PostDto.class);

/*        PostDto postResponse = new PostDto();
        postResponse.setId(newPost.getId());
        postResponse.setContent(newPost.getContent());
        postResponse.setTitle(newPost.getTitle());
        postResponse.setDescription(newPost.getDescription());*/

        return postResponse;
    }

}
