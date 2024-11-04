package com.nusiss.neighbourlysg.controller;


import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.LikeDto;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.repository.LikeRepository;
import com.nusiss.neighbourlysg.service.LikeService;
import com.nusiss.neighbourlysg.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class LikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LikeService likeService;

    @Mock
    LikeRepository likeRepository;

    private LikeController likeController;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        likeController = new LikeController(likeService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(likeController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void testLikePost() throws Exception {
        Long profileId = 1L;
        Long postId = 1L;

        LikeDto likeDto = new LikeDto();
        likeDto.setId(1L);
        likeDto.setProfileId(profileId);
        likeDto.setPostId(postId);
        likeDto.setLikedAt(LocalDateTime.now());

        when(likeService.likePost(profileId, postId)).thenReturn(likeDto);

        mockMvc.perform(post("/api/LikeService/{profileId}/posts/{postId}", profileId, postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(likeDto.getId()))
                .andExpect(jsonPath("$.profileId").value(likeDto.getProfileId()))
                .andExpect(jsonPath("$.postId").value(likeDto.getPostId()));
    }

    @Test
    void testUnlikePost() throws Exception {
        Long profileId = 1L;
        Long postId = 1L;

        mockMvc.perform(delete("/api/LikeService/{profileId}/posts/{postId}", profileId, postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(likeService).unlikePost(profileId, postId);
    }

    @Test
    void testGetLikeCount() throws Exception {
        Long postId = 1L;
        int likeCount = 10;

        when(likeService.getLikeCount(postId)).thenReturn(likeCount);

        mockMvc.perform(get("/api/LikeService/posts/{postId}/count", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(likeCount));
    }

    @Test
    void testIsPostLikedByProfile() throws Exception {
        Long profileId = 1L;
        Long postId = 1L;
        boolean isLiked = true;

        when(likeService.isPostLikedByProfile(profileId, postId)).thenReturn(isLiked);

        mockMvc.perform(get("/api/LikeService/{profileId}/posts/{postId}/isLiked", profileId, postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(isLiked));
    }

    @Test
    void testGetProfilesWhoLikedPost() throws Exception {
        Long postId = 1L;

        ProfileDto profileDto1 = new ProfileDto();
        profileDto1.setId(1L);
        profileDto1.setName("User 1");
        profileDto1.setEmail("user1@example.com");
        profileDto1.setConstituency("Constituency 1");
        profileDto1.setRoles(Set.of("1, 2"));

        ProfileDto profileDto2 = new ProfileDto();
        profileDto2.setId(2L);
        profileDto2.setName("User 2");
        profileDto2.setEmail("user2@example.com");
        profileDto2.setConstituency("Constituency 2");
        profileDto2.setRoles(Set.of("1"));

        List<ProfileDto> profilesWhoLiked = List.of(profileDto1, profileDto2);

        when(likeService.getProfilesWhoLikedPost(postId)).thenReturn(profilesWhoLiked);

        mockMvc.perform(get("/api/LikeService/{postId}/likes/profiles", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(profileDto1.getId()))
                .andExpect(jsonPath("$[0].name").value(profileDto1.getName()))
                .andExpect(jsonPath("$[0].email").value(profileDto1.getEmail()))
                .andExpect(jsonPath("$[0].constituency").value(profileDto1.getConstituency()))
                .andExpect(jsonPath("$[1].id").value(profileDto2.getId()))
                .andExpect(jsonPath("$[1].name").value(profileDto2.getName()))
                .andExpect(jsonPath("$[1].email").value(profileDto2.getEmail()))
                .andExpect(jsonPath("$[1].constituency").value(profileDto2.getConstituency()));
    }
}
