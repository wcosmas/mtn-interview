package com.mtn.uganda.interview.interview;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AppConfiguration appConfig;

    public PostService(PostRepository postRepository, UserRepository userRepository,
                       AppConfiguration appConfig) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.appConfig = appConfig;
    }

    public Page<PostDto> getAllPosts(int page, int size) {

        size = Math.min(size, appConfig.getMaxPageSize());
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(this::convertToDto);
    }

    public PostDto getPostById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return convertToDto(post);
    }

    public List<PostDto> getPostsByUserId(Long userId) {

        // Verify user exists
        if (!userRepository.existsById(Math.toIntExact(userId))) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PostDto createPost(PostDto postDto) {

        // Verify user exists
        if (!userRepository.existsById(Math.toIntExact(postDto.getUserId()))) {
            throw new ResourceNotFoundException("User not found with id: " + postDto.getUserId());
        }

        Post post = convertToEntity(postDto);
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    public PostDto updatePost(Long id, PostDto postDto) {

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        existingPost.setTitle(postDto.getTitle());
        existingPost.setBody(postDto.getBody());


        Post updatedPost = postRepository.save(existingPost);
        return convertToDto(updatedPost);
    }

    public void deletePost(Long id) {

        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        postRepository.deleteById(id);
    }

    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setBody(post.getBody());
        postDto.setTitle(post.getTitle());
        postDto.setUserId(post.getUserId());
        post.setCreatedAt(post.getCreatedAt());
        post.setUpdatedAt(post.getUpdatedAt());



        // Add username if user is loaded
        if (post.getUser() != null) {
            postDto.setUsername(post.getUser().getUsername());
        }

        return postDto;
    }

    private Post convertToEntity(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setUserId(postDto.getUserId());
        post.setTitle(postDto.getTitle());
        post.setBody(postDto.getBody());
        post.setCreatedAt(postDto.getCreatedAt());
        post.setUpdatedAt(postDto.getUpdatedAt());
        return post;
    }
}