package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;

public class ProfileMapper {

    public static ProfileDto mapToDto(Profile profile) {

        ProfileDto profileDto = new ProfileDto();
        profileDto.setEmail(profile.getEmail());
        profileDto.setFirstName(profile.getFirstName());
        profileDto.setLastName(profile.getLastName());
        profileDto.setUserName(profile.getUserName());
        profileDto.setContactNumber(profile.getContactNumber());

        return profileDto;
    }

    public static Profile mapToProfile(ProfileDto profileDto)
    {
        Profile profile = new Profile();
        profile.setEmail(profileDto.getEmail());
        profile.setFirstName(profileDto.getFirstName());
        profile.setLastName(profileDto.getLastName());
        profile.setUserName(profileDto.getUserName());
        profile.setContactNumber(profileDto.getContactNumber());

        return profile;
    }
}
