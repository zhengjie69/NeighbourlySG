package com.nusiss.neighbourlysg.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.LikeDto;
import com.nusiss.neighbourlysg.entity.Like;
import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.PostAlreadyLikedByProfileException;
import com.nusiss.neighbourlysg.repository.LikeRepository;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.impl.LikeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        likeService = new LikeServiceImpl(likeRepository, postRepository, profileRepository);

    }

    @Test
    void testLikePostSuccess() {
        Long profileId = 1L;
        Long postId = 1L;

        Profile profile = new Profile();
        profile.setId(profileId);

        Post post = new Post();
        post.setId(postId);
        post.setLikesCount(0);

        Like like = new Like();
        like.setProfile(profile);
        like.setPost(post);
        like.setLikedAt(LocalDateTime.now());

        Like savedLike = new Like();
        savedLike.setId(1L);
        savedLike.setProfile(profile);
        savedLike.setPost(post);
        savedLike.setLikedAt(LocalDateTime.now());

        when(likeRepository.findByProfileIdAndPostId(profileId, postId)).thenReturn(Optional.empty());
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likeRepository.save(any(Like.class))).thenReturn(savedLike);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        LikeDto result = likeService.likePost(profileId, postId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(profileId, result.getProfileId());
        assertEquals(postId, result.getPostId());
        assertNotNull(result.getLikedAt());

        verify(likeRepository).findByProfileIdAndPostId(profileId, postId);
        verify(profileRepository).findById(profileId);
        verify(postRepository).findById(postId);
        verify(likeRepository).save(any(Like.class));
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testLikePostAlreadyLiked() {
        Long profileId = 1L;
        Long postId = 1L;

        when(likeRepository.findByProfileIdAndPostId(profileId, postId)).thenReturn(Optional.of(new Like()));

        assertThrows(PostAlreadyLikedByProfileException.class, () -> likeService.likePost(profileId, postId));

        verify(likeRepository).findByProfileIdAndPostId(profileId, postId);
        verifyNoMoreInteractions(profileRepository, postRepository, likeRepository);
    }

    @Test
    void testUnlikePostSuccess() {
        Long profileId = 1L;
        Long postId = 1L;

        Profile profile = new Profile();
        profile.setId(profileId);

        Post post = new Post();
        post.setId(postId);
        post.setLikesCount(1);

        Like like = new Like();
        like.setProfile(profile);
        like.setPost(post);

        when(likeRepository.findByProfileIdAndPostId(profileId, postId)).thenReturn(Optional.of(like));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        likeService.unlikePost(profileId, postId);

        assertEquals(0, post.getLikesCount());

        verify(likeRepository).findByProfileIdAndPostId(profileId, postId);
        verify(postRepository).findById(postId);
        verify(likeRepository).delete(like);
        verify(postRepository).save(post);
    }

    @Test
    void testUnlikePostNotFound() {
        Long profileId = 1L;
        Long postId = 1L;

        when(likeRepository.findByProfileIdAndPostId(profileId, postId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> likeService.unlikePost(profileId, postId));

        verify(likeRepository).findByProfileIdAndPostId(profileId, postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testGetLikeCount() {
        Long postId = 1L;
        int expectedCount = 5;

        when(likeRepository.countByPostId(postId)).thenReturn(expectedCount);

        int likeCount = likeService.getLikeCount(postId);

        assertEquals(expectedCount, likeCount);

        verify(likeRepository).countByPostId(postId);
    }

    @Test
    void testIsPostLikedByProfile() {
        Long profileId = 1L;
        Long postId = 1L;

        when(likeRepository.findByProfileIdAndPostId(profileId, postId)).thenReturn(Optional.of(new Like()));

        boolean isLiked = likeService.isPostLikedByProfile(profileId, postId);

        assertTrue(isLiked);

        verify(likeRepository).findByProfileIdAndPostId(profileId, postId);
    }
}
