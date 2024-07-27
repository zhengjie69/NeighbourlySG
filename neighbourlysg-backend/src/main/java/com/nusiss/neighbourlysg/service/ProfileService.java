package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.ProfileDto;

import java.util.List;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto profileDto);

    ProfileDto getProfileById(Long id);

    List<ProfileDto> getAllProfile();

    ProfileDto updateProfile(Long id, ProfileDto profileDto);

    void deleteProfile(Long id);
}
