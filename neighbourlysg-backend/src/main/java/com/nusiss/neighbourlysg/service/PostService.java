package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(Long profileId, PostDto postDto);

    PostDto getPostById(Long postId);
    List<PostDto> getAllPostsByProfile(Long profileId);
    PostDto updatePost(Long postId, PostDto postDto);
    void deletePost(Long postId);
    PostDto likePost(Long postId, Long profileId);
    CommentDto createComment(Long postId, Long profileId, CommentDto commentDto);
    List<CommentDto> getCommentsByPost(Long postId);
    CommentDto updateCommentOnPost(Long postId, Long commentId, CommentDto commentDto);
    void deleteCommentOnPost(Long postId, Long commentId);

    CommentDto getCommentById(Long commentId);
}