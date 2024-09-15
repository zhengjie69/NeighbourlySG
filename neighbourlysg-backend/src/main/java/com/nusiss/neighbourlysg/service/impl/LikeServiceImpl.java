package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.LikeDto;
import com.nusiss.neighbourlysg.entity.Like;
import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.PostNotFoundException;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.repository.LikeRepository;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.LikeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
        Optional<Like> existingLike = likeRepository.findByProfileIdAndPostId(profileId, postId);
        if (existingLike.isPresent()) {
            throw new RuntimeException("Post is already liked by the profile");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Like like = new Like();
        like.setProfile(profile);
        like.setPost(post);
        like.setLikedAt(LocalDateTime.now());

        Like savedLike = likeRepository.save(like);

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
}
