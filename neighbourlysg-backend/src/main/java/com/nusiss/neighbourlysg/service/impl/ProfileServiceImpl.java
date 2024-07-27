package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.ResourceNotFoundException;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.ProfileService;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;

    @Override
    public ProfileDto createProfile(ProfileDto profileDto) {

        Profile profile = ProfileMapper.mapToProfile(profileDto);
        Profile savedProfile = profileRepository.save(profile);

        return ProfileMapper.mapToDto(savedProfile);
    }

    @Override
    public ProfileDto getProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile Not Found with the given id: " + id));

        return ProfileMapper.mapToDto(profile);
    }

    @Override
    public List<ProfileDto> getAllProfile() {
        List<Profile> listOfProfile = profileRepository.findAll();
        return listOfProfile.stream().map(ProfileMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProfileDto updateProfile(Long id, ProfileDto updatedProfileDto) {

        Profile retrievedProfile = profileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile which want to be updated cannot be found" + id));

        retrievedProfile.setContactNumber(updatedProfileDto.getContactNumber());
        retrievedProfile.setLastName(updatedProfileDto.getLastName());
        retrievedProfile.setFirstName(updatedProfileDto.getFirstName());
        retrievedProfile.setContactNumber(updatedProfileDto.getContactNumber());
        retrievedProfile.setUserName(updatedProfileDto.getUserName());

        Profile updatedProfile = profileRepository.save(retrievedProfile);

        return ProfileMapper.mapToDto(updatedProfile);
    }

    @Override
    public void deleteProfile(Long id) {

        Profile retrievedProfile = profileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile which want to be deleted cannot be found" + id));

        profileRepository.deleteById(id);
    }

}
