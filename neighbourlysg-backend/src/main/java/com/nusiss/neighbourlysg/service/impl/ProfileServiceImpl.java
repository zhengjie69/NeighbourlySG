package com.nusiss.neighbourlysg.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.exception.ResourceNotFoundException;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.EmailInUseException;
import com.nusiss.neighbourlysg.exception.PasswordWrongException;
import com.nusiss.neighbourlysg.exception.UserNotExistedException;
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

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper, RoleRepository roleRepository) {
    	this.profileRepository=profileRepository;
    	this.profileMapper=profileMapper;
        this.roleRepository = roleRepository;
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
        //String encodedPassword = passwordEncoder.encode(password);
	
        Profile profile = profileMapper.toEntity(profileDto);
        profile.setRoles(role);
        Profile savedProfile = profileRepository.save(profile);

        return profileMapper.toDto(savedProfile);
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
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));

        // Update fields only if they are present in the DTO
        if (profileDto.getName() != null && !Objects.equals(profileDto.getName(), existingProfile.getName())) {
            existingProfile.setName(profileDto.getName());
        }
        if (profileDto.getEmail() != null && !Objects.equals(profileDto.getEmail(), existingProfile.getEmail())) {
            existingProfile.setEmail(profileDto.getEmail());
        }
        if (profileDto.getConstituency() != null && !Objects.equals(profileDto.getConstituency(), existingProfile.getConstituency())) {
            existingProfile.setConstituency(profileDto.getConstituency());
        }
        if (profileDto.getPassword() != null && !profileDto.getPassword().isEmpty() ) {
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
    public ProfileDto login(LoginRequestDTO loginRequestDTO) {
    	// Check if user with email, check password
    	
    	Optional<Profile> profileOp = profileRepository.findByEmail(loginRequestDTO.getEmail());
    	
        if (profileOp.isPresent()) {
            //check password
        	if(!StringUtils.equals(profileOp.get().getPassword(), loginRequestDTO.getPassword())) {
        		 throw new PasswordWrongException();
        	}
        }else {
        	throw new UserNotExistedException();
        } 
        
        return profileMapper.toDto(profileOp.get());

    }

    @Override
    public void deleteProfile(Long profileId) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with ID " + profileId + " cannot be found"));

        profileRepository.deleteById(profileId);
    }

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
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + roleAssignmentDto.getUserId()));

        Role role = roleRepository.findById(roleAssignmentDto.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleAssignmentDto.getRoleId()));

        // Check if the profile already has the role
        if (profile.getRoles().contains(role)) {
            throw new RuntimeException("User already has this role");
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
