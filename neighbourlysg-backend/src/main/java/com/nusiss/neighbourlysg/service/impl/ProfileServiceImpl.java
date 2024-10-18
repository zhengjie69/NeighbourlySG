package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.common.RoleConstants;
import com.nusiss.neighbourlysg.config.ErrorMessagesConstants;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.exception.*;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper,
                              RoleRepository roleRepository, PasswordEncoder encoder) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public ProfileDto createProfile(ProfileDto profileDto) throws RoleNotFoundException {

        // Check if user with email already exists, since email is unique
        //email is also username
        profileDto.setUsername(profileDto.getEmail());

        if (profileRepository.findByEmail(profileDto.getEmail()).isPresent()) {
            throw new EmailInUseException();
        }

        profileDto.setPassword(encoder.encode(profileDto.getPassword()));

        Set<String> strRoles = profileDto.getRoles();
        Set<Role> roles = new HashSet<>();
        createOrUpdateRolesForProfile(strRoles, roles);

        Profile profile = profileMapper.toEntity(profileDto);
        profile.setRoles(roles);
        Profile savedProfile = profileRepository.save(profile);

        return profileMapper.toDto(savedProfile);
    }

    private void createOrUpdateRolesForProfile(Set<String> strRoles, Set<Role> roles) throws RoleNotFoundException {
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleConstants.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("Default USER role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case RoleConstants.ROLE_ADMIN:
                        Role adminRole = roleRepository.findByName(RoleConstants.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: ADMIN Role is not found."));
                        roles.add(adminRole);

                        break;
                    case RoleConstants.ROLE_ORGANISER:
                        Role modRole = roleRepository.findByName(RoleConstants.ROLE_ORGANISER)
                                .orElseThrow(() -> new RuntimeException("Error: ORGANISER Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleConstants.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: USER Role is not found."));
                        roles.add(userRole);
                }
            });
        }
    }

    @Override
    public ProfileDto getProfileById(Long profileId) {
        if(profileId == null) {
            throw new IllegalArgumentException("No Profile Id is inputted");
        }
        Optional<Profile> profile = profileRepository.findById(profileId);

        if(profile.isPresent()) {
            return profileMapper.toDto(profile.get());
        }else{
            throw new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId);
        }
    }

    @Override
    public ProfileDto updateProfile(Long id, ProfileDto profileDto) throws RoleNotFoundException {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + id));

        // Update fields only if they are present in the DTO
        if (StringUtils.hasText(profileDto.getName()) && !Objects.equals(profileDto.getName(), existingProfile.getName())) {
            existingProfile.setName(profileDto.getName());
        }
        if (StringUtils.hasText(profileDto.getEmail()) && !Objects.equals(profileDto.getEmail(), existingProfile.getEmail())) {
            existingProfile.setEmail(profileDto.getEmail());
            existingProfile.setUsername(profileDto.getEmail());
        }
        if (StringUtils.hasText(profileDto.getConstituency()) && !Objects.equals(profileDto.getConstituency(), existingProfile.getConstituency())) {
            existingProfile.setConstituency(profileDto.getConstituency());
        }
        if (profileDto.getPassword() != null && !profileDto.getPassword().isEmpty()) {
            existingProfile.setPassword(profileDto.getPassword());
        }

        // Update roles if provided
        if (profileDto.getRoles() != null && !profileDto.getRoles().isEmpty()) {// Update roles if provided
            Set<String> strRoles = profileDto.getRoles();
            Set<Role> roles = new HashSet<>();
            createOrUpdateRolesForProfile(strRoles, roles);
            existingProfile.setRoles(roles);
        }

        // Save the updated profile
        Profile updatedProfile = profileRepository.save(existingProfile);

        return profileMapper.toDto(updatedProfile);
    }

    @Override
    public void deleteProfile(Long profileId) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        profileRepository.delete(profile); // Use delete() with the profile entity
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
        Set<Role> existingRoles = new HashSet<>(profile.getRoles());

        // Determine which roles to add and which to remove
        Set<Role> rolesToAdd = newRoles.stream()
                .filter(role -> !existingRoles.contains(role))
                .collect(Collectors.toSet());
        Set<Role> rolesToRemove = existingRoles.stream()
                .filter(role -> !newRoles.contains(role))
                .collect(Collectors.toSet());

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

        Set<Role> existingRoles = new HashSet<>(profile.getRoles()); // Ensure modifiable list
        List<Role> newRoles = findRoleByIds(roleIds);

        // Find roles to add
        Set<Role> rolesToAdd = newRoles.stream()
                .filter(role -> !existingRoles.contains(role))
                .collect(Collectors.toSet());

        // Find roles to remove
        Set<Role> rolesToRemove = existingRoles.stream()
                .filter(role -> !newRoles.contains(role))
                .collect(Collectors.toSet());

        // Update roles
        existingRoles.addAll(rolesToAdd);
        existingRoles.removeAll(rolesToRemove);

        profile.setRoles(existingRoles); // Set the updated list of roles to the profile

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
    public boolean isAdmin(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));

        // Check if any of the user's roles has the ID 3 (admin role)
        return profile.getRoles().stream()
                .anyMatch(role -> role.getId() == 3);
    }

    @Override
    public Profile findById(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(ErrorMessagesConstants.PROFILE_NOT_FOUND + profileId));
    }

}
