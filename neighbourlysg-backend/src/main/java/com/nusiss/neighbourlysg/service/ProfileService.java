package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.*;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto profileDto) throws RoleNotFoundException;

    ProfileDto login(LoginRequestDTO loginRequestDTO);

    void deleteProfile(Long profileId);

    List<ProfileDto> getAllProfiles();

    ProfileDto getProfileById(Long profileId);

    ProfileDto updateProfile(Long id, ProfileDto profileDto) throws RoleNotFoundException;

    ProfileDto assignRoleToUser(RoleAssignmentDto roleAssignmentDto) throws RoleNotFoundException, ProfileNotFoundException;

    ProfileDto updateRoles(Long userId, List<Integer> roleIds) throws RoleNotFoundException, ProfileNotFoundException;

    // Methods to handle posts, likes, and comments
    PostDto createPost(Long profileId, PostDto postDto);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPostsByProfile(Long profileId);

    PostDto updatePost(Long postId, PostDto postDto);

    void deletePost(Long postId);

    // New methods for handling likes and comments
    PostDto likePost(Long postId, Long profileId);

    CommentDto createComment(Long postId, Long profileId, CommentDto commentDto);

    List<CommentDto> getCommentsByPost(Long postId);

    CommentDto updateCommentOnPost(Long postId, Long commentId, CommentDto commentDto);

    void deleteCommentOnPost(Long postId, Long commentId);
}
