package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.mapper.PostMapper;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.service.PostService;
import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/PostService")
public class PostController {

    private final PostService postService;
    private final ProfileService profileService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;

    public PostController(PostService postService, ProfileService profileService, PostMapper postMapper, PostRepository postRepository) {
        this.postService = postService;
        this.profileService = profileService;
        this.postMapper = postMapper;
        this.postRepository = postRepository;
    }

    // Create a new post
    @PostMapping("/api/PostService/{profileId}")
    public ResponseEntity<PostDto> createPost(@PathVariable Long profileId, @RequestBody PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        post.setProfile(profileService.findById(profileId)); // Set profile
        postRepository.save(post);
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    // Get a post by its ID
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    // Get all posts by a profile
    @GetMapping("/{profileId}")
    public ResponseEntity<List<PostDto>> getAllPostsByProfile(@PathVariable Long profileId) {
        List<PostDto> posts = postService.getAllPostsByProfile(profileId);
        return ResponseEntity.ok(posts);
    }

    // Update a post
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(postId, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    // Delete a post
    @DeleteMapping("/{postId}/{profileId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @PathVariable Long profileId) {
        // Fetch the post to check its owner
        PostDto post = postService.getPostById(postId);

        // Check if the user is the owner of the post or an admin
        if (post.getProfileId().equals(profileId) || profileService.isAdmin(profileId)) {
            postService.deletePost(postId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Forbidden if not admin or post owner
        }
    }

    // Endpoint to like a post (increment like count)
    @PostMapping("/{postId}/like/{profileId}")
    public ResponseEntity<PostDto> likePost(@PathVariable Long postId, @PathVariable Long profileId) {
        PostDto likedPost = postService.likePost(postId, profileId);
        return ResponseEntity.ok(likedPost);
    }

    // Add a comment to a post
    @PostMapping("/{postId}/comments/{profileId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @PathVariable Long profileId, @RequestBody CommentDto commentDto) {
        CommentDto createdComment = postService.createComment(postId, profileId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // Update a comment on a post
    @PutMapping("/{postId}/comments/{commentId}/{profileId}")
    public ResponseEntity<CommentDto> updateCommentOnPost(@PathVariable Long postId, @PathVariable Long commentId, @PathVariable Long profileId, @RequestBody CommentDto commentDto) {
        // Fetch the comment to check its owner
        CommentDto existingComment = postService.getCommentById(commentId);

        if (!existingComment.getProfileId().equals(profileId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Only the comment owner can update
        }

        CommentDto updatedComment = postService.updateCommentOnPost(postId, commentId, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    // Delete a comment on a post
    @DeleteMapping("/{postId}/comments/{commentId}/{profileId}")
    public ResponseEntity<Void> deleteCommentOnPost(@PathVariable Long postId, @PathVariable Long commentId, @PathVariable Long profileId) {
        // Fetch the comment to check its owner and post owner
        CommentDto existingComment = postService.getCommentById(commentId);
        PostDto post = postService.getPostById(postId);

        // Check if the user is the post owner, comment owner, or an admin
        boolean isPostOwner = post.getProfileId().equals(profileId);
        boolean isCommentOwner = existingComment.getProfileId().equals(profileId);
        boolean isAdmin = profileService.isAdmin(profileId); // Assuming you have a method like this

        if (!isPostOwner && !isCommentOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Only post owner, comment owner, or admin can delete
        }

        postService.deleteCommentOnPost(postId, commentId);
        return ResponseEntity.noContent().build();
    }

    // Get all comments for a post
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto> comments = postService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    // Returns list of posts with the searched list of tags
    @GetMapping("/by-tags")
    public ResponseEntity<List<PostDto>> getPostsByTags(@RequestParam List<String> tags) {
        List<PostDto> posts = postService.getPostsByTags(tags);
        return ResponseEntity.ok(posts);
    }
}
