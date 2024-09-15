package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.LikeDto;

public interface LikeService {
    LikeDto likePost(Long profileId, Long postId);
    void unlikePost(Long profileId, Long postId);
    int getLikeCount(Long postId);
    boolean isPostLikedByProfile(Long profileId, Long postId);
}
