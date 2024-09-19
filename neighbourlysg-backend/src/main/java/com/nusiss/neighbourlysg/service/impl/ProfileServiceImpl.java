package com.nusiss.neighbourlysg.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.nusiss.neighbourlysg.common.RoleConstants;
import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.exception.*;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper, RoleRepository roleRepository,
                              PasswordEncoder encoder) {
    	this.profileRepository=profileRepository;
    	this.profileMapper=profileMapper;
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
        //encode the password
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
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleConstants.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: ADMIN Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "organiser":
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
            throw new ProfileNotFoundException("Profile not found with id: " + profileId);
        }
    }

    @Override
    public ProfileDto updateProfile(Long id, ProfileDto profileDto) throws RoleNotFoundException {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile is not found while updating: " + id));

        // Update fields only if they are present in the DTO
        if (profileDto.getName() != null && !Objects.equals(profileDto.getName(), existingProfile.getName())) {
            existingProfile.setName(profileDto.getName());
        }

        if (profileDto.getEmail() != null && !Objects.equals(profileDto.getEmail(), existingProfile.getEmail())) {
            existingProfile.setEmail(profileDto.getEmail());
            existingProfile.setUsername(profileDto.getEmail());
        }
        if (profileDto.getConstituency() != null && !Objects.equals(profileDto.getConstituency(), existingProfile.getConstituency())) {
            existingProfile.setConstituency(profileDto.getConstituency());
        }
        if (profileDto.getPassword() != null && !profileDto.getPassword().isEmpty() ) {
            existingProfile.setPassword(profileDto.getPassword());
        }
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
                .orElseThrow(() -> new ProfileNotFoundException("Profile with ID " + profileId + " cannot be found"));

        profileRepository.deleteById(profile.getId());
    }


//    @Override
//    public ProfileDto login(LoginRequestDTO loginRequestDTO) {
//    	// Check if user with email, check password
//
//    	Optional<Profile> profileOp = profileRepository.findByEmail(loginRequestDTO.getEmail());
//
//        if (profileOp.isPresent()) {
//            //check password
//        	if(!StringUtils.equals(profileOp.get().getPassword(), loginRequestDTO.getPassword())) {
//        		 throw new PasswordWrongException();
//        	}
//        }else {
//        	throw new UserNotExistedException();
//        }
//
//        return profileMapper.toDto(profileOp.get());
//
//    }

    @Override
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll(); // Fetch all profiles from the repository
        return profiles.stream()
                .map(profileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfileDto assignRoleToUser(RoleAssignmentDto roleAssignmentDto) throws RoleNotFoundException, ProfileNotFoundException {
        Profile profile = profileRepository.findById(roleAssignmentDto.getUserId())
                .orElseThrow(() -> new ProfileNotFoundException("Profile is not found when assigning role to user of ID: " + roleAssignmentDto.getUserId()));

        Role role = roleRepository.findById(roleAssignmentDto.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role is not found when assigning role to user of ID: " + roleAssignmentDto.getRoleId()));

        // Check if the profile already has the role
        if (profile.getRoles().contains(role)) {
            throw new RoleAlreadyExistsException("User already has this role");
        }

        // Add the new role to the profile
        profile.getRoles().add(role);
        Profile updatedProfile = profileRepository.save(profile);

        return profileMapper.toDto(updatedProfile);
    }

    private List<Role> findRoleByIds(List<Integer> roleIds) throws RoleNotFoundException {
        List<Role> roles = new ArrayList<>();

        for (Integer roleId : roleIds) {
            Optional<Role> existingRole = roleRepository.findById(roleId);
            if (existingRole.isPresent()) {
                roles.add(existingRole.get());
            } else {
                throw new RoleNotFoundException("Role with ID " + roleId + " not found");
            }
        }

        return roles;
    }

}
