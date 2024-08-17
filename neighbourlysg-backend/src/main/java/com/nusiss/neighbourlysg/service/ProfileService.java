package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;

import javax.management.relation.RoleNotFoundException;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto profileDto) throws RoleNotFoundException;

    ProfileDto login(LoginRequestDTO loginRequestDTO);

    ProfileDto updateProfile(Long id, ProfileDto profileDto) throws RoleNotFoundException;

    ProfileDto assignRoleToUser(RoleAssignmentDto roleAssignmentDto) throws RoleNotFoundException, ProfileNotFoundException;
}
