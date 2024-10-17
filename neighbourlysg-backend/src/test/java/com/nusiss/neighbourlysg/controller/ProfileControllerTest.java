package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProfiles() {
        ProfileDto profileDto = new ProfileDto();
        when(profileService.getAllProfiles()).thenReturn(Collections.singletonList(profileDto));

        ResponseEntity<List<ProfileDto>> response = profileController.getAllProfiles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetProfileByIdSuccess() {
        Long id = 1L;
        ProfileDto profileDto = new ProfileDto();
        when(profileService.getProfileById(id)).thenReturn(profileDto);

        ResponseEntity<ProfileDto> response = profileController.getProfileById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDto, response.getBody());
    }

    @Test
    void testGetProfileByIdNotFound() {
        Long id = 1L;
        when(profileService.getProfileById(id)).thenThrow(new ProfileNotFoundException("Profile not found"));

        ResponseEntity<ProfileDto> response = profileController.getProfileById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGetProfileByIdInternalServerError() {
        Long id = 1L;

        // Simulate an unexpected exception being thrown
        when(profileService.getProfileById(id)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<ProfileDto> response = profileController.getProfileById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testUpdateProfileSuccess() throws RoleNotFoundException {
        Long id = 1L;
        ProfileDto updatedProfile = new ProfileDto();
        when(profileService.updateProfile(id, updatedProfile)).thenReturn(updatedProfile);

        ResponseEntity<ProfileDto> response = profileController.updateProfile(id, updatedProfile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }

    @Test
    void testUpdateProfileNotFound() throws RoleNotFoundException {
        Long id = 1L;
        ProfileDto updatedProfile = new ProfileDto();
        when(profileService.updateProfile(id, updatedProfile)).thenThrow(new ProfileNotFoundException("Profile not found"));

        ResponseEntity<ProfileDto> response = profileController.updateProfile(id, updatedProfile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testUpdateProfileInternalServerError() throws RoleNotFoundException {
        Long id = 1L;
        ProfileDto updatedProfile = new ProfileDto();

        // Simulate an unexpected exception being thrown
        when(profileService.updateProfile(id, updatedProfile)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<ProfileDto> response = profileController.updateProfile(id, updatedProfile);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
    @Test
    void testAssignRoleToUserSuccess() throws RoleNotFoundException, ProfileNotFoundException {
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
        ProfileDto updatedProfile = new ProfileDto();

        // Mocking the service call
        when(profileService.updateRoles(roleAssignmentDto.getUserId(), roleAssignmentDto.getRoleIds()))
                .thenReturn(updatedProfile);

        // Executing the method under test
        ResponseEntity<Object> response = profileController.assignRoleToUser(roleAssignmentDto);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAssignRoleToUser_RoleNotFound() throws RoleNotFoundException {
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();

        // Mocking the service call to throw an exception
        when(profileService.updateRoles(roleAssignmentDto.getUserId(), roleAssignmentDto.getRoleIds()))
                .thenThrow(new RoleNotFoundException("Role does not exist"));

        // Executing the method under test
        ResponseEntity<Object> response = profileController.assignRoleToUser(roleAssignmentDto);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Role not found: Role does not exist", response.getBody());
    }

    @Test
    void testAssignRoleToUser_ProfileNotFound() throws RoleNotFoundException {
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();

        // Mocking the service call to throw an exception
        when(profileService.updateRoles(roleAssignmentDto.getUserId(), roleAssignmentDto.getRoleIds()))
                .thenThrow(new ProfileNotFoundException("Profile does not exist"));

        // Executing the method under test
        ResponseEntity<Object> response = profileController.assignRoleToUser(roleAssignmentDto);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Profile not found: Profile does not exist", response.getBody());
    }

    @Test
    void testAssignRoleToUser_RuntimeException() throws RoleNotFoundException {
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();

        // Mocking the service call to throw an exception
        when(profileService.updateRoles(roleAssignmentDto.getUserId(), roleAssignmentDto.getRoleIds()))
                .thenThrow(new RuntimeException("Some unexpected error"));

        // Executing the method under test
        ResponseEntity<Object> response = profileController.assignRoleToUser(roleAssignmentDto);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error occurred while updating roles", response.getBody());
    }

    @Test
    void testDeleteProfileSuccess() {
        Long id = 1L;
        doNothing().when(profileService).deleteProfile(id);

        ResponseEntity<String> response = profileController.deleteProfile(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile deleted successfully!", response.getBody());
    }

    @Test
    void testDeleteProfileNotFound() {
        Long id = 1L;
        doThrow(new ProfileNotFoundException("Profile not found")).when(profileService).deleteProfile(id);

        ResponseEntity<String> response = profileController.deleteProfile(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteProfileInternalServerError() {
        Long id = 1L;

        // Simulate an unexpected exception being thrown
        doThrow(new RuntimeException("Unexpected error")).when(profileService).deleteProfile(id);

        ResponseEntity<String> response = profileController.deleteProfile(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
