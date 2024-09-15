package com.nusiss.neighbourlysg.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nusiss.neighbourlysg.config.ErrorMessagesConstants;
import com.nusiss.neighbourlysg.dto.*;
import com.nusiss.neighbourlysg.entity.Comment;
import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.exception.*;
import com.nusiss.neighbourlysg.mapper.CommentMapper;
import com.nusiss.neighbourlysg.mapper.PostMapper;
import com.nusiss.neighbourlysg.repository.CommentRepository;
import com.nusiss.neighbourlysg.repository.PostRepository;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.EmailInUseException;
import com.nusiss.neighbourlysg.exception.PasswordWrongException;
import com.nusiss.neighbourlysg.exception.UserNotExistedException;
import com.nusiss.neighbourlysg.exception.RoleNotFoundException;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;  // Add PostRepository
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper,
                              RoleRepository roleRepository, PostRepository postRepository, PostMapper postMapper, CommentMapper commentMapper, CommentRepository commentRepository) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.roleRepository = roleRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ProfileDto createProfile(ProfileDto profileDto) throws RoleNotFoundException {
        // Check if user with email already exists, since email is unique
        if (profileRepository.findByEmail(profileDto.getEmail()).isPresent()) {
            throw new EmailInUseException();
        }

        List<Role> role;
        if (profileDto.getRoles() == null || profileDto.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RoleNotFoundException("Default role not found"));
            role = List.of(defaultRole);
        } else {
            role = findRoleByIds(profileDto.getRoles());
        }

        // Encrypt the password
        // String encodedPassword = passwordEncoder.encode(password);

        Profile profile = profileMapper.toEntity(profileDto);
        profile.setRoles(role);
        Profile savedProfile = profileRepository.save(profile);

        return profileMapper.toDto(savedProfile);
    }

    @Override
    public ProfileDto getProfileById(Long profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("No Profile Id is inputted");
        }
        Optional<Profile> profile = profileRepository.findById(profileId);

        if (profile.isPresent()) {
            return profileMapper.toDto(profile.get());
        } else {
            throw new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId);
        }
    }

    @Override
    public ProfileDto updateProfile(Long id, ProfileDto profileDto) throws RoleNotFoundException {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + id));

        // Update fields only if they are present in the DTO
        if (profileDto.getName() != null && !Objects.equals(profileDto.getName(), existingProfile.getName())) {
            existingProfile.setName(profileDto.getName());
        }
        if (profileDto.getEmail() != null && !Objects.equals(profileDto.getEmail(), existingProfile.getEmail())) {
            existingProfile.setEmail(profileDto.getEmail());
        }
        if (profileDto.getConstituency() != null
                && !Objects.equals(profileDto.getConstituency(), existingProfile.getConstituency())) {
            existingProfile.setConstituency(profileDto.getConstituency());
        }
        if (profileDto.getPassword() != null && !profileDto.getPassword().isEmpty()) {
            existingProfile.setPassword(profileDto.getPassword());
        }

        // Update roles if provided
        if (profileDto.getRoles() != null && !profileDto.getRoles().isEmpty()) {
            List<Role> roles = findRoleByIds(profileDto.getRoles());
            existingProfile.setRoles(roles);
        }

        // Save the updated profile
        Profile updatedProfile = profileRepository.save(existingProfile);

        return profileMapper.toDto(updatedProfile);
    }

    @Override
    public void deleteProfile(Long profileId) {

        profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        profileRepository.deleteById(profileId);
    }

    @Override
    public ProfileDto login(LoginRequestDTO loginRequestDTO) {
        // Check if user with email, check password

        Optional<Profile> profileOp = profileRepository.findByEmail(loginRequestDTO.getEmail());

        if (profileOp.isPresent()) {
            // check password
            if (!StringUtils.equals(profileOp.get().getPassword(), loginRequestDTO.getPassword())) {
                throw new PasswordWrongException();
            }
        } else {
            throw new UserNotExistedException();
        }

        return profileMapper.toDto(profileOp.get());

    }

    @Override
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll(); // Fetch all profiles from the repository
        return profiles.stream()
                .map(profileMapper::toDto)
                .toList();
    }

    @Override
    public ProfileDto assignRoleToUser(RoleAssignmentDto roleAssignmentDto)
            throws RoleNotFoundException, ProfileNotFoundException {
        // Retrieve the profile by userId, throw exception if not found
        Profile profile = profileRepository.findById(roleAssignmentDto.getUserId())
                .orElseThrow(() -> new ProfileNotFoundException(
                        ErrorMessagesConstants.PROFILE_NOT_FOUND + roleAssignmentDto.getUserId()));

        // Fetch roles by roleIds and throw exception if any role is not found
        List<Role> newRoles = roleAssignmentDto.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RoleNotFoundException(ErrorMessagesConstants.ROLE_NOT_FOUND + roleId)))
                .toList();

        // Retrieve the existing roles from the profile
        List<Role> existingRoles = new ArrayList<>(profile.getRoles());

        // Determine which roles to add and which to remove
        List<Role> rolesToAdd = newRoles.stream()
                .filter(role -> !existingRoles.contains(role))
                .toList();
        List<Role> rolesToRemove = existingRoles.stream()
                .filter(role -> !newRoles.contains(role))
                .toList();

        // Update the profile's roles
        existingRoles.addAll(rolesToAdd);
        existingRoles.removeAll(rolesToRemove);
        profile.setRoles(existingRoles);

        // Save the updated profile
        Profile updatedProfile = profileRepository.save(profile);

        return profileMapper.toDto(updatedProfile);
    }

    @Override
    @Transactional
    public ProfileDto updateRoles(Long userId, List<Integer> roleIds)
            throws RoleNotFoundException, ProfileNotFoundException {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + userId));

        List<Role> existingRoles = profile.getRoles();
        List<Role> newRoles = findRoleByIds(roleIds);

        // Find roles to add
        List<Role> rolesToAdd = newRoles.stream()
                .filter(role -> !existingRoles.contains(role))
                .toList();

        // Find roles to remove
        List<Role> rolesToRemove = existingRoles.stream()
                .filter(role -> !newRoles.contains(role))
                .toList();

        // Update roles
        profile.getRoles().addAll(rolesToAdd);
        profile.getRoles().removeAll(rolesToRemove);

        Profile updatedProfile = profileRepository.save(profile);
        return profileMapper.toDto(updatedProfile);
    }

    private List<Role> findRoleByIds(List<Integer> roleIds) throws RoleNotFoundException {
        List<Role> roles = new ArrayList<>();
        for (Integer roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RoleNotFoundException(ErrorMessagesConstants.ROLE_NOT_FOUND + roleId));
            roles.add(role);
        }
        return roles;
    }

    @Override
    public PostDto createPost(Long profileId, PostDto postDto) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setCreationDate(LocalDateTime.now());
        post.setProfile(profile);

        post.setLikeCount(0);  // Initialize like count to 0
        post.setComments(new ArrayList<>());  // Initialize with an empty comment list

        Post savedPost = postRepository.save(post);

        return postMapper.toDto(savedPost);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> getAllPostsByProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        return postRepository.findByProfile(profile).stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        post.setContent(postDto.getContent());
        post.setCreationDate(postDto.getCreationDate());
        post.setLikeCount(postDto.getLikeCount());

        // Ensure postDto.getComments() is a List<CommentDto>
        if (!postDto.getComments().isEmpty()) { // Check if comments are not null
            List<Comment> comments = postDto.getComments().stream()
                    .map(commentMapper::toEntity) // Properly map CommentDto to Comment
                    .collect(Collectors.toList());
            post.setComments(comments);
        }

        Post updatedPost = postRepository.save(post);
        return postMapper.toDto(updatedPost);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));
        postRepository.delete(post);
    }

    @Override
    public PostDto likePost(Long postId, Long profileId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        // Increment like count
        post.setLikeCount(post.getLikeCount() + 1);

        // Save the updated post
        Post updatedPost = postRepository.save(post);

        // Return the updated post as a DTO
        return postMapper.toDto(updatedPost);
    }

    @Override
    public CommentDto createComment(Long postId, Long profileId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        Comment comment = commentMapper.toEntity(commentDto);
        comment.setPost(post);
        comment.setProfile(profile);
        comment.setCreationDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        return commentRepository.findByPost(post).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto updateCommentOnPost(Long postId, Long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessagesConstants.COMMENT_NOT_FOUND + commentId));

        if (!post.getComments().contains(comment)) {
            throw new CommentNotFoundException("Comment not found in the specified post.");
        }

        // Update the comment's content
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);

        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteCommentOnPost(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessagesConstants.POST_NOT_FOUND + commentId));

        if (!post.getComments().contains(comment)) {
            throw new CommentNotFoundException("Comment not found in the specified post.");
        }

        post.getComments().remove(comment);
        commentRepository.delete(comment);
        postRepository.save(post);
    }

}
