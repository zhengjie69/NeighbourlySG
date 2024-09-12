package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.RoleDto;

import java.time.LocalDate;
import java.util.Arrays;

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
        profile.setRoles(Arrays.asList(1));
        profile.setName("name");
        profile.setPassword("password");
        return profile;
    }

    public static RoleDto createRoleDTO() {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName("USER");
        return roleDto;
    }

    public static EventDto createEventDTO(){

        EventDto eventDto = new EventDto();
        eventDto.setRsvpCount(1L);
        eventDto.setLocation("testLocation");
        eventDto.setDescription("testDescription");
        eventDto.setTitle("testTitle");
        eventDto.setStartTime("testStartTime");
        eventDto.setEndTime("testEndTime");
        eventDto.setDate(LocalDate.now());

        return eventDto;
    }

}
