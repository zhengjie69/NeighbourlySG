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
    void testAssignRoleToUserSuccess() throws RoleNotFoundException {
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
        ProfileDto updatedProfile = new ProfileDto();
        when(profileService.assignRoleToUser(roleAssignmentDto)).thenReturn(updatedProfile);

        ResponseEntity<ProfileDto> response = profileController.assignRoleToUser(roleAssignmentDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProfile, response.getBody());
    }

    @Test
    void testAssignRoleToUserBadRequest() throws RoleNotFoundException {
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
        when(profileService.assignRoleToUser(roleAssignmentDto)).thenThrow(new RoleNotFoundException("Role not found"));

        ResponseEntity<ProfileDto> response = profileController.assignRoleToUser(roleAssignmentDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
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
