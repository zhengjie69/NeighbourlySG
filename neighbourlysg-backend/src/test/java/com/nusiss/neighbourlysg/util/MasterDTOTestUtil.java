package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;

public final class MasterDTOTestUtil {

    public static LoginRequestDTO createLoginRequestDTO(){
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("email");
        loginRequestDTO.setPassword("password");
        return loginRequestDTO;
    }

    public static ProfileDto createProfileDTO() {
        ProfileDto profile = new ProfileDto();
        profile.setConstituency("con");
        profile.setEmail("email");
        profile.setId(1L);
        profile.setIsAdmin(true);
        profile.setName("name");
        profile.setPassword("password");
        return profile;
    }

}
