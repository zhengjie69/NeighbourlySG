package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.entity.Comment;
import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.CommentNotFoundException;
import com.nusiss.neighbourlysg.exception.PostNotFoundException;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.mapper.CommentMapper;
import com.nusiss.neighbourlysg.mapper.PostMapper;
import com.nusiss.neighbourlysg.repository.CommentRepository;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostServiceImpl(postRepository, profileRepository, commentRepository, postMapper, commentMapper);
    }

    @Test
    void testCreatePostSuccess() {
        Long profileId = 1L;
        PostDto postDto = new PostDto();
        postDto.setContent("Post Content");

        Profile profile = new Profile();
        profile.setId(profileId);

        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setCreationDate(LocalDateTime.now());
        post.setProfile(profile);
        post.setLikesCount(0);
        post.setComments(new ArrayList<>());

        Post savedPost = new Post();
        savedPost.setId(1L);
        savedPost.setContent(post.getContent());
        savedPost.setCreationDate(post.getCreationDate());
        savedPost.setProfile(profile);
        savedPost.setLikesCount(0);
        savedPost.setComments(post.getComments());

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(postMapper.toDto(savedPost)).thenReturn(postDto);

        PostDto result = postService.createPost(profileId, postDto);

        assertNotNull(result);
        assertEquals(postDto.getContent(), result.getContent());

        verify(profileRepository).findById(profileId);
        verify(postRepository).save(any(Post.class));
        verify(postMapper).toDto(savedPost);
    }

    @Test
    void testCreatePostProfileNotFound() {
        Long profileId = 1L;
        PostDto postDto = new PostDto();

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> postService.createPost(profileId, postDto));

        verify(profileRepository).findById(profileId);
        verifyNoMoreInteractions(postRepository, postMapper);
    }

    @Test
    void getAllPosts_ShouldReturnListOfPostDtos() {
        // Create mock Post entities
        Post post1 = new Post();
        post1.setId(1L);
        post1.setContent("Post Content 1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setContent("Post Content 2");

        List<Post> posts = Arrays.asList(post1, post2);

        // Create corresponding PostDto objects
        PostDto postDto1 = new PostDto();
        postDto1.setId(1L);
        postDto1.setContent("Post Content 1");

        PostDto postDto2 = new PostDto();
        postDto2.setId(2L);
        postDto2.setContent("Post Content 2");

        List<PostDto> expectedPostDtos = Arrays.asList(postDto1, postDto2);

        // Mock the repository and mapper behaviors
        when(postRepository.findAll()).thenReturn(posts);
        when(postMapper.toDto(post1)).thenReturn(postDto1);
        when(postMapper.toDto(post2)).thenReturn(postDto2);

        // Call the service method
        List<PostDto> result = postService.getAllPosts();

        // Assertions to check if the result matches the expected output
        assertEquals(expectedPostDtos.size(), result.size());
        assertEquals(expectedPostDtos.get(0).getId(), result.get(0).getId());
        assertEquals(expectedPostDtos.get(0).getContent(), result.get(0).getContent());
        assertEquals(expectedPostDtos.get(1).getId(), result.get(1).getId());
        assertEquals(expectedPostDtos.get(1).getContent(), result.get(1).getContent());

        // Verify the repository and mapper interactions
        verify(postRepository, times(1)).findAll();
        verify(postMapper, times(1)).toDto(post1);
        verify(postMapper, times(1)).toDto(post2);
    }

    @Test
    void testGetPostByIdSuccess() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        PostDto postDto = new PostDto();
        postDto.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);

        PostDto result = postService.getPostById(postId);

        assertNotNull(result);
        assertEquals(postId, result.getId());

        verify(postRepository).findById(postId);
        verify(postMapper).toDto(post);
    }

    @Test
    void testGetPostByIdNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostById(postId));

        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(postMapper);
    }

    @Test
    void testUpdatePostSuccess() {
        Long postId = 1L;
        PostDto postDto = new PostDto();
        postDto.setContent("Updated Content");
        postDto.setCreationDate(LocalDateTime.now());
        postDto.setLikeCount(5);
        postDto.setComments(new ArrayList<>());

        Post post = new Post();
        post.setId(postId);
        post.setContent("Old Content");
        post.setCreationDate(LocalDateTime.now().minusDays(1));
        post.setLikesCount(0);
        post.setComments(new ArrayList<>());

        Post updatedPost = new Post();
        updatedPost.setId(postId);
        updatedPost.setContent(postDto.getContent());
        updatedPost.setCreationDate(postDto.getCreationDate());
        updatedPost.setLikesCount(postDto.getLikeCount());
        updatedPost.setComments(new ArrayList<>());

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);
        when(postMapper.toDto(updatedPost)).thenReturn(postDto);

        PostDto result = postService.updatePost(postId, postDto);

        assertNotNull(result);
        assertEquals(postDto.getContent(), result.getContent());
        assertEquals(postDto.getLikeCount(), result.getLikeCount());

        verify(postRepository).findById(postId);
        verify(postRepository).save(any(Post.class));
        verify(postMapper).toDto(updatedPost);
    }

    @Test
    void testUpdatePostNotFound() {
        Long postId = 1L;
        PostDto postDto = new PostDto();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.updatePost(postId, postDto));

        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(postRepository, postMapper);
    }

    @Test
    void testDeletePostSuccess() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.deletePost(postId);

        verify(postRepository).findById(postId);
        verify(postRepository).delete(post);
    }

    @Test
    void testDeletePostNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.deletePost(postId));

        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testCreateCommentSuccess() {
        Long postId = 1L;
        Long profileId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Comment Content");

        Post post = new Post();
        post.setId(postId);

        Profile profile = new Profile();
        profile.setId(profileId);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setProfile(profile);
        comment.setCreationDate(LocalDateTime.now());

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent(commentDto.getContent());
        savedComment.setCreationDate(comment.getCreationDate());
        savedComment.setPost(post);
        savedComment.setProfile(profile);

        CommentDto savedCommentDto = new CommentDto();
        savedCommentDto.setId(1L);
        savedCommentDto.setContent(commentDto.getContent());
        savedCommentDto.setPostId(postId);
        savedCommentDto.setProfileId(profileId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toDto(savedComment)).thenReturn(savedCommentDto);

        CommentDto result = postService.createComment(postId, profileId, commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getContent(), result.getContent());
        assertEquals(postId, result.getPostId());
        assertEquals(profileId, result.getProfileId());

        verify(postRepository).findById(postId);
        verify(profileRepository).findById(profileId);
        verify(commentMapper).toEntity(commentDto);
        verify(commentRepository).save(comment);
        verify(commentMapper).toDto(savedComment);
    }

    @Test
    void testCreateCommentPostNotFound() {
        Long postId = 1L;
        Long profileId = 1L;
        CommentDto commentDto = new CommentDto();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.createComment(postId, profileId, commentDto));

        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(profileRepository, commentMapper, commentRepository);
    }

    @Test
    void testCreateCommentProfileNotFound() {
        Long postId = 1L;
        Long profileId = 1L;
        CommentDto commentDto = new CommentDto();

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> postService.createComment(postId, profileId, commentDto));

        verify(postRepository).findById(postId);
        verify(profileRepository).findById(profileId);
        verifyNoMoreInteractions(commentMapper, commentRepository);
    }

    @Test
    void testGetCommentsByPostSuccess() {
        Long postId = 1L;
        Comment comment = new Comment();
        comment.setId(1L);
        List<Comment> comments = List.of(comment);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(commentRepository.findByPost(any(Post.class))).thenReturn(comments);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        List<CommentDto> result = postService.getCommentsByPost(postId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commentDto.getId(), result.get(0).getId());

        verify(postRepository).findById(postId);
        verify(commentRepository).findByPost(any(Post.class));
        verify(commentMapper).toDto(comment);
    }

    @Test
    void testGetCommentsByPostNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getCommentsByPost(postId));

        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(commentRepository, commentMapper);
    }

    @Test
    void testUpdateCommentOnPostNotFound() {
        Long postId = 1L;
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto();

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> postService.updateCommentOnPost(postId, commentId, commentDto));

        verify(postRepository).findById(postId);
        verify(commentRepository).findById(commentId);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void testDeleteCommentOnPostSuccess() {
        Long postId = 1L;
        Long commentId = 1L;

        Post post = new Post();
        post.setId(postId);
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPost(post);

        // Initialize the post's comments list and add the comment
        post.setComments(new ArrayList<>());
        post.getComments().add(comment);

        // Set up mock behavior
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Call the service method
        postService.deleteCommentOnPost(postId, commentId);

        // Verify interactions
        verify(postRepository).findById(postId);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).delete(comment);
        verify(postRepository).save(post);
    }

    @Test
    void testDeleteCommentOnPostNotFound() {
        Long postId = 1L;
        Long commentId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> postService.deleteCommentOnPost(postId, commentId));

        verify(postRepository).findById(postId);
        verify(commentRepository).findById(commentId);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void testGetCommentByIdSuccess() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto result = postService.getCommentById(commentId);

        assertNotNull(result);
        assertEquals(commentId, result.getId());

        verify(commentRepository).findById(commentId);
        verify(commentMapper).toDto(comment);
    }

    @Test
    void testGetCommentByIdNotFound() {
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> postService.getCommentById(commentId));

        verify(commentRepository).findById(commentId);
        verifyNoMoreInteractions(commentMapper);
    }

    @Test
    void testGetPostsByTags() {
        List<String> tags = List.of("tag1", "tag2");
        Post post = new Post();
        post.setTags(List.of("tag1", "tag2"));

        PostDto postDto = new PostDto();
        postDto.setTags(tags);

        when(postRepository.findAll()).thenReturn(List.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);

        List<PostDto> result = postService.getPostsByTags(tags);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTags().containsAll(tags));

        verify(postRepository).findAll();
        verify(postMapper).toDto(post);
    }

    @Test
    void testGetPostsByTagsEmptyList() {
        List<String> tags = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> postService.getPostsByTags(tags));

        verifyNoMoreInteractions(postRepository, postMapper);
    }
}

