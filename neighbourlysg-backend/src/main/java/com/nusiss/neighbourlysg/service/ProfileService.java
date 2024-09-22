package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.*;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto profileDto) throws RoleNotFoundException;

    ProfileDto login(LoginRequestDTO loginRequestDTO);

    void deleteProfile(Long profileId);

    List<ProfileDto> getAllProfiles();

    ProfileDto getProfileById(Long profileId);

    ProfileDto updateProfile(Long id, ProfileDto profileDto) throws RoleNotFoundException;

    ProfileDto assignRoleToUser(RoleAssignmentDto roleAssignmentDto) throws RoleNotFoundException, ProfileNotFoundException;

    ProfileDto updateRoles(Long userId, List<Integer> roleIds) throws RoleNotFoundException, ProfileNotFoundException;

    boolean isAdmin(Long profileId);

    Profile findById(Long profileId);
}
