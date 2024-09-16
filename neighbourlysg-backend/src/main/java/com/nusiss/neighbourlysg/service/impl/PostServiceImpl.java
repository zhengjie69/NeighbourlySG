package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.config.ErrorMessagesConstants;
import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.entity.Comment;
import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.CommentNotFoundException;
import com.nusiss.neighbourlysg.exception.PostNotFoundException;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.mapper.CommentMapper;
import com.nusiss.neighbourlysg.mapper.PostMapper;
import com.nusiss.neighbourlysg.repository.CommentRepository;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public PostServiceImpl(PostRepository postRepository, ProfileRepository profileRepository,
                           CommentRepository commentRepository, PostMapper postMapper, CommentMapper commentMapper) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.commentRepository = commentRepository;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public PostDto createPost(Long profileId, PostDto postDto) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setCreationDate(LocalDateTime.now());
        post.setProfile(profile);

        post.setLikesCount(0);  // Initialize like count to 0
        post.setComments(new ArrayList<>());  // Initialize with an empty comment list

        Post savedPost = postRepository.save(post);

        return postMapper.toDto(savedPost);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> getAllPostsByProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        return postRepository.findByProfile(profile).stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        post.setContent(postDto.getContent());
        post.setCreationDate(postDto.getCreationDate());
        post.setLikesCount(postDto.getLikeCount());

        // Ensure postDto.getComments() is a List<CommentDto>
        if (!postDto.getComments().isEmpty()) { // Check if comments are not null
            List<Comment> comments = postDto.getComments().stream()
                    .map(commentMapper::toEntity) // Properly map CommentDto to Comment
                    .toList();
            post.setComments(comments);
        }

        Post updatedPost = postRepository.save(post);
        return postMapper.toDto(updatedPost);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));
        postRepository.delete(post);
    }

    @Override
    public CommentDto createComment(Long postId, Long profileId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPost(post);
        comment.setProfile(profile);
        comment.setCreationDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        return commentRepository.findByPost(post).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto updateCommentOnPost(Long postId, Long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessagesConstants.COMMENT_NOT_FOUND + commentId));

        if (!post.getComments().contains(comment)) {
            throw new CommentNotFoundException("Comment not found in the specified post.");
        }

        // Update the comment's content
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteCommentOnPost(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + commentId));

        if (!post.getComments().contains(comment)) {
            throw new CommentNotFoundException("Comment not found in the specified post.");
        }

        post.getComments().remove(comment);
        commentRepository.delete(comment);
        postRepository.save(post);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        // Find the comment by ID from the repository
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessagesConstants.COMMENT_NOT_FOUND + commentId));

        // Convert the comment entity to a DTO using CommentMapper
        return commentMapper.toDto(comment);
    }

    @Override
    public List<PostDto> getPostsByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Tags list cannot be null or empty");
        }

        // Retrieve all posts from the repository
        List<Post> allPosts = postRepository.findAll();

        // Filter posts that contain all the specified tags
        List<Post> filteredPosts = allPosts.stream()
                .filter(post -> post.getTags() != null && post.getTags().containsAll(tags))
                .toList();

        // Convert the filtered posts to DTOs
        return filteredPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }

}
