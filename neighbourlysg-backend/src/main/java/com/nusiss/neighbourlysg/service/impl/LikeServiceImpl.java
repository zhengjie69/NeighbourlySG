package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.LikeDto;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Like;
import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.PostAlreadyLikedByProfileException;
import com.nusiss.neighbourlysg.exception.PostNotFoundException;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.repository.LikeRepository;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.LikeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;

    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, ProfileRepository profileRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public LikeDto likePost(Long profileId, Long postId) {
        // Check if the post is already liked by the profile
        Optional<Like> existingLike = likeRepository.findByProfileIdAndPostId(profileId, postId);
        if (existingLike.isPresent()) {
            throw new PostAlreadyLikedByProfileException("Post is already liked by the profile");
        }

        // Retrieve the profile and post
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        // Create and save the like
        Like like = new Like();
        like.setProfile(profile);
        like.setPost(post);
        like.setLikedAt(LocalDateTime.now());

        Like savedLike = likeRepository.save(like);

        // Increment like count in the Post entity
        post.setLikesCount(post.getLikesCount() + 1);

        // Save the updated Post entity
        postRepository.save(post);

        // Create and return LikeDto
        LikeDto likeDto = new LikeDto();
        likeDto.setId(savedLike.getId());
        likeDto.setProfileId(savedLike.getProfile().getId());
        likeDto.setPostId(savedLike.getPost().getId());
        likeDto.setLikedAt(savedLike.getLikedAt());

        return likeDto;
    }

    @Override
    public void unlikePost(Long profileId, Long postId) {
        Like like = likeRepository.findByProfileIdAndPostId(profileId, postId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        // Retrieve the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        // Decrement like count in the Post entity
        post.setLikesCount(post.getLikesCount() - 1);

        // Save the updated Post entity
        postRepository.save(post);

        // Delete the like
        likeRepository.delete(like);
    }

    @Override
    public int getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public boolean isPostLikedByProfile(Long profileId, Long postId) {
        return likeRepository.findByProfileIdAndPostId(profileId, postId).isPresent();
    }

    @Override
    public List<ProfileDto> getProfilesWhoLikedPost(Long postId) {
        // Retrieve all Likes for the given postId
        List<Like> likes = likeRepository.findByPostId(postId);

        // Convert List<Like> to List<ProfileDto>
        return likes.stream()
                .map(like -> {
                    ProfileDto profileDto = new ProfileDto();
                    profileDto.setId(like.getProfile().getId());
                    profileDto.setName(like.getProfile().getName());
                    profileDto.setEmail(like.getProfile().getEmail());
                    profileDto.setConstituency(like.getProfile().getConstituency());
                     Set<String> roleIds = like.getProfile().getRoles().stream()
                        .map(role -> String.valueOf(role.getId()))
                        .collect(Collectors.toSet());
                        profileDto.setRoles(roleIds);

                    return profileDto;
                })
                .toList();
    }
}
