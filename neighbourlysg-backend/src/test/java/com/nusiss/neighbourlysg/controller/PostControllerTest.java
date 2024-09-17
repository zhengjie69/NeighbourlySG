package com.nusiss.neighbourlysg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.mapper.PostMapper;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.PostService;
import com.nusiss.neighbourlysg.service.ProfileService;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @Mock
    private ProfileService profileService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ProfileRepository profileRepository;

    private PostController postController;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postController = new PostController(postService, profileService, postMapper, postRepository);
        this.mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void testGetPostById() throws Exception {
        Long postId = 1L;
        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setContent("Post Content");

        when(postService.getPostById(postId)).thenReturn(postDto);

        mockMvc.perform(get("/api/PostService/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.content").value(postDto.getContent()));
    }

    @Test
    void testGetAllPostsByProfile() throws Exception {
        Long profileId = 1L;
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        List<PostDto> posts = Collections.singletonList(postDto);

        when(postService.getAllPostsByProfile(profileId)).thenReturn(posts);

        mockMvc.perform(get("/api/PostService/profile/{profileId}", profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(postDto.getId()))
                .andExpect(jsonPath("$[0].content").value(postDto.getContent()));
    }

    @Test
    void testCreatePost() throws Exception {
        Long profileId = 1L;

        PostDto postDto = MasterDTOTestUtil.createPostDTO();
        Post post = new Post(); // Create a valid Post object

        when(postMapper.toEntity(any(PostDto.class))).thenReturn(post);
        when(profileService.findById(profileId)).thenReturn(new Profile());

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(postDto);

        mockMvc.perform(post("/api/PostService/{profileId}", profileId)
                        .contentType("application/json")
                        .content(objectToJson))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePost() throws Exception {
        Long postId = 1L;
        PostDto updatedPost = MasterDTOTestUtil.createPostDTO();
        PostDto updatedPostResponse = MasterDTOTestUtil.createPostDTO(); // Simulate a successful update

        when(postService.updatePost(any(), any(PostDto.class))).thenReturn(updatedPostResponse);

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(updatedPost);

        mockMvc.perform(put("/api/PostService/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePost() throws Exception {
        Long postId = 1L;
        Long profileId = 1L;

        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setProfileId(profileId);

        when(postService.getPostById(postId)).thenReturn(postDto);
        when(profileService.isAdmin(profileId)).thenReturn(false);

        mockMvc.perform(delete("/api/PostService/{postId}/{profileId}", postId, profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(postService).deletePost(postId);
    }

    @Test
    void testDeleteCommentOnPost() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;
        Long profileId = 1L;

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        commentDto.setProfileId(profileId);

        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setProfileId(profileId);

        when(postService.getCommentById(commentId)).thenReturn(commentDto);
        when(postService.getPostById(postId)).thenReturn(postDto);
        when(profileService.isAdmin(profileId)).thenReturn(false);

        mockMvc.perform(delete("/api/PostService/{postId}/comments/{commentId}/{profileId}", postId, commentId, profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(postService).deleteCommentOnPost(postId, commentId);
    }

    @Test
    void testGetCommentsByPost() throws Exception {
        Long postId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        List<CommentDto> comments = Collections.singletonList(commentDto);

        when(postService.getCommentsByPost(postId)).thenReturn(comments);

        mockMvc.perform(get("/api/PostService/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(commentDto.getId()))
                .andExpect(jsonPath("$[0].content").value(commentDto.getContent()));
    }

    @Test
    void testGetPostsByTags() throws Exception {
        List<String> tags = List.of("tag1", "tag2");
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        List<PostDto> posts = Collections.singletonList(postDto);

        when(postService.getPostsByTags(tags)).thenReturn(posts);

        mockMvc.perform(get("/api/PostService/by-tags")
                        .param("tags", "tag1", "tag2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(postDto.getId()));
    }

    @Test
    void testCreateComment() throws Exception {
        Long postId = 1L;
        Long profileId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Comment Content");

        CommentDto createdCommentDto = new CommentDto();
        createdCommentDto.setId(1L);
        createdCommentDto.setContent("Comment Content");

        when(postService.createComment(postId, profileId, commentDto)).thenReturn(createdCommentDto);

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(commentDto);

        mockMvc.perform(post("/api/PostService/{postId}/comments/{profileId}", postId, profileId)
                        .contentType("application/json")
                        .content(objectToJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateCommentOnPost() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;
        Long profileId = 1L;

        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setId(commentId);
        updatedCommentDto.setContent("Updated Comment");
        updatedCommentDto.setProfileId(profileId);  // Set profileId

        when(postService.getCommentById(commentId)).thenReturn(updatedCommentDto);
        when(postService.updateCommentOnPost(postId, commentId, updatedCommentDto)).thenReturn(updatedCommentDto);

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(updatedCommentDto);

        mockMvc.perform(put("/api/PostService/{postId}/comments/{commentId}/{profileId}", postId, commentId, profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePostForbidden() throws Exception {
        Long postId = 1L;
        Long profileId = 2L; // Different profileId to test forbidden access

        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setProfileId(1L); // Different profileId to test forbidden access

        when(postService.getPostById(postId)).thenReturn(postDto);
        when(profileService.isAdmin(profileId)).thenReturn(false);

        mockMvc.perform(delete("/api/PostService/{postId}/{profileId}", postId, profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


}