package com.nusiss.neighbourlysg.controller;


import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.LikeDto;
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
}
