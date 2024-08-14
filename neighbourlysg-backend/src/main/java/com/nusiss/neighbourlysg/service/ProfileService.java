package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto profileDto);

    ProfileDto login(LoginRequestDTO loginRequestDTO);

    ProfileDto updateProfile(Long id, ProfileDto profileDto);
}
