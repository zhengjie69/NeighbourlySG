package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.service.ProfileService;
import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.dto.CommentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/PostService")
public class PostController {

    private final ProfileService profileService;

    public PostController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Create a new post
    @PostMapping("/{profileId}")
    public ResponseEntity<PostDto> createPost(@PathVariable Long profileId, @RequestBody PostDto postDto) {
        PostDto createdPost = profileService.createPost(profileId, postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // Get a post by its ID
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        PostDto postDto = profileService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    // Get all posts by a profile
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<PostDto>> getAllPostsByProfile(@PathVariable Long profileId) {
        List<PostDto> posts = profileService.getAllPostsByProfile(profileId);
        return ResponseEntity.ok(posts);
    }

    // Update a post
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
        PostDto updatedPost = profileService.updatePost(postId, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    // Delete a post
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        profileService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to like a post (increment like count)
    @PostMapping("/{postId}/like/{profileId}")
    public ResponseEntity<PostDto> likePost(@PathVariable Long postId, @PathVariable Long profileId) {
        PostDto likedPost = profileService.likePost(postId, profileId);
        return ResponseEntity.ok(likedPost);
    }

    // Add a comment to a post
    @PostMapping("/{postId}/comments/{profileId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @PathVariable Long profileId, @RequestBody CommentDto commentDto) {
        CommentDto createdComment = profileService.createComment(postId, profileId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // Update a comment on a post
    @PutMapping("/{postId}/comments/{commentId}/{profileId}")
    public ResponseEntity<CommentDto> updateCommentOnPost(@PathVariable Long postId, @PathVariable Long commentId, @PathVariable Long profileId, @RequestBody CommentDto commentDto) {
        // Fetch the comment to check its owner
        CommentDto existingComment = profileService.getCommentById(commentId);

        if (!existingComment.getProfileId().equals(profileId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Only the comment owner can update
        }

        CommentDto updatedComment = profileService.updateCommentOnPost(postId, commentId, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    // Delete a comment on a post
    @DeleteMapping("/{postId}/comments/{commentId}/{profileId}")
    public ResponseEntity<Void> deleteCommentOnPost(@PathVariable Long postId, @PathVariable Long commentId, @PathVariable Long profileId) {
        // Fetch the comment to check its owner and post owner
        CommentDto existingComment = profileService.getCommentById(commentId);
        PostDto post = profileService.getPostById(postId);

        if (!post.getProfileId().equals(profileId) || !existingComment.getProfileId().equals(profileId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Only post owner or comment owner can delete
        }

        profileService.deleteCommentOnPost(postId, commentId);
        return ResponseEntity.noContent().build();
    }

    // Get all comments for a post
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto> comments = profileService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }
}
