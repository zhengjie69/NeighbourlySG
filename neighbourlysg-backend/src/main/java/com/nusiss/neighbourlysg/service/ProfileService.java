package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;

import java.util.List;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto profileDto);

	ProfileDto login(LoginRequestDTO loginRequestDTO);
}
