package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.LikeDto;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/LikeService")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // Like a post
    @PostMapping("/{profileId}/posts/{postId}")
    public ResponseEntity<LikeDto> likePost(@PathVariable Long profileId, @PathVariable Long postId) {
        LikeDto likedPost = likeService.likePost(profileId, postId);
        return ResponseEntity.ok(likedPost);
    }

    // Unlike a post
    @DeleteMapping("/{profileId}/posts/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long profileId, @PathVariable Long postId) {
        likeService.unlikePost(profileId, postId);
        return ResponseEntity.noContent().build();
    }

    // Get like count for a post
    @GetMapping("/posts/{postId}/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long postId) {
        int likeCount = likeService.getLikeCount(postId);
        return ResponseEntity.ok(likeCount);
    }

    // Check if a post is liked by a profile
    @GetMapping("/{profileId}/posts/{postId}/isLiked")
    public ResponseEntity<Boolean> isPostLikedByProfile(@PathVariable Long profileId, @PathVariable Long postId) {
        boolean isLiked = likeService.isPostLikedByProfile(profileId, postId);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/{postId}/likes/profiles")
    public ResponseEntity<List<ProfileDto>> getProfilesWhoLikedPost(@PathVariable Long postId) {
        List<ProfileDto> profilesWhoLiked = likeService.getProfilesWhoLikedPost(postId);
        return ResponseEntity.ok(profilesWhoLiked);
    }
}