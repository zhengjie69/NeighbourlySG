package com.nusiss.neighbourlysg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.ProfileService;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class ProfileControllerTest {
    @Mock
    ProfileService profileService;
    @Mock
    ProfileRepository profileRepository;

    private MockMvc mockMvc;

    private ProfileController profileController;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        profileController = new ProfileController(profileService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void createProfileTest() throws Exception {
        ProfileDto profileDto=MasterDTOTestUtil.createProfileDTO();
        when(profileRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(profileService.createProfile(any())).thenReturn(profileDto);

        byte[] data = TestUtil.convertObjectToJsonBytes(profileDto);

        mockMvc.perform(post("/api/" + "/ProfileService/register")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8).content(data)).andExpect(status().isOk());
    }

    @Test
    void getProfileTest() throws Exception {
        ProfileDto profileDto=MasterDTOTestUtil.createProfileDTO();

        when(profileService.getProfileById(any())).thenReturn(profileDto);

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(profileDto);

        mockMvc .perform(get("/api/" + "/ProfileService/profile/{id}", profileDto.getId()).contentType(TestUtil.APPLICATION_JSON_UTF8).content(objectToJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(profileDto.getId()))
                .andExpect(jsonPath("$.email").value(profileDto.getEmail()))
                .andExpect(jsonPath("$.constituency").value(profileDto.getConstituency()))
                .andExpect(jsonPath("$.name").value(profileDto.getName()));
    }

    @Test
    void getProfileTest_ProfileNotFoundException() throws Exception {

        ProfileDto profileDto=MasterDTOTestUtil.createProfileDTO();

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(profileDto);

        when(profileService.getProfileById(any())).thenThrow(ProfileNotFoundException.class);

        mockMvc .perform(get("/api/" + "/ProfileService/profile/{id}", profileDto.getId()).contentType(TestUtil.APPLICATION_JSON_UTF8).content(objectToJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProfileTest_RunTimeException() throws Exception {
        // Given
        ProfileDto profileDto=MasterDTOTestUtil.createProfileDTO();

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(profileDto);

        when(profileService.getProfileById(any())).thenThrow(new RuntimeException("Simulated exception"));

        mockMvc .perform(get("/api/" + "/ProfileService/profile/{id}", profileDto.getId()).contentType(TestUtil.APPLICATION_JSON_UTF8).content(objectToJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllProfiles_ShouldReturnProfiles() throws Exception {
        // Given
        ProfileDto profile1 = MasterDTOTestUtil.createProfileDTO();
        List<ProfileDto> profiles = List.of(profile1);

        when(profileService.getAllProfiles()).thenReturn(profiles);

        // When & Then
        mockMvc.perform(get("/api/ProfileService/profiles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(profile1.getId())); // Adjust based on ProfileDto's properties
    }

    @Test
    void updateProfile_ShouldReturnOkWhenProfileIsUpdated() throws Exception {
        // Given
        Long profileId = 1L;
        ProfileDto updatedProfile = MasterDTOTestUtil.createProfileDTO(); // Ensure this utility method creates a valid ProfileDto
        ProfileDto updatedProfileResponse = MasterDTOTestUtil.createProfileDTO(); // Simulate a successful update

        when(profileService.updateProfile(any(), any(ProfileDto.class))).thenReturn(updatedProfileResponse);

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(updatedProfile); // Ensure this utility method converts the ProfileDto to JSON bytes

        // When & Then
        mockMvc.perform(put("/api/ProfileService/updateProfile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson))
                .andExpect(status().isOk());
    }

    @Test
    void updateProfile_ShouldReturnNotFoundWhenProfileNotFoundExceptionOccurs() throws Exception {
        // Given
        Long profileId = 1L;
        ProfileDto updatedProfile = MasterDTOTestUtil.createProfileDTO();

        when(profileService.updateProfile(any(), any(ProfileDto.class))).thenThrow(new ProfileNotFoundException("Profile not found"));

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(updatedProfile);

        // When & Then
        mockMvc.perform(put("/api/ProfileService/updateProfile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProfile_ShouldReturnInternalServerErrorWhenOtherExceptionOccurs() throws Exception {
        // Given
        Long profileId = 1L;
        ProfileDto updatedProfile = MasterDTOTestUtil.createProfileDTO();

        when(profileService.updateProfile(any(), any(ProfileDto.class))).thenThrow(new RuntimeException("Simulated exception"));

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(updatedProfile);

        // When & Then
        mockMvc.perform(put("/api/ProfileService/updateProfile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void assignRoleToUser_ShouldReturnOkWhenRoleIsAssigned() throws Exception {
        // Given
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
        roleAssignmentDto.setUserId(1L);
        roleAssignmentDto.setRoleIds(Arrays.asList(1, 2, 3)); // Updated for List<Integer>

        ProfileDto updatedProfile = new ProfileDto();
        updatedProfile.setId(1L); // Populate with test data as needed

        when(profileService.updateRoles(any(Long.class), any(List.class))).thenReturn(updatedProfile);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(roleAssignmentDto);

        // When & Then
        mockMvc.perform(put("/api/ProfileService/assign-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void assignRoleToUser_ShouldReturnBadRequestWhenOtherRuntimeExceptionOccurs() throws Exception {
        // Given
        RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
        roleAssignmentDto.setUserId(1L);
        roleAssignmentDto.setRoleIds(Arrays.asList(1, 2, 3)); // Updated for List<Integer>

        when(profileService.updateRoles(any(Long.class), any(List.class)))
                .thenThrow(new RuntimeException("Simulated exception"));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(roleAssignmentDto);

        // When & Then
        mockMvc.perform(put("/api/ProfileService/assign-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProfile_ShouldReturnOkWhenProfileIsDeleted() throws Exception {
        // Given
        Long profileId = 1L;

        // Simulate successful profile deletion
        doNothing().when(profileService).deleteProfile(profileId);

        // When & Then
        mockMvc.perform(delete("/api/ProfileService/profile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProfile_ShouldReturnNotFoundWhenProfileNotFoundExceptionOccurs() throws Exception {
        // Given
        Long profileId = 1L;

        // Simulate that ProfileNotFoundException is thrown
        doThrow(new ProfileNotFoundException("Profile not found")).when(profileService).deleteProfile(profileId);

        // When & Then
        mockMvc.perform(delete("/api/ProfileService/profile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProfile_ShouldReturnInternalServerErrorWhenOtherExceptionOccurs() throws Exception {
        // Given
        Long profileId = 1L;

        // Simulate that a generic exception is thrown
        doThrow(new RuntimeException("Simulated exception")).when(profileService).deleteProfile(profileId);

        // When & Then
        mockMvc.perform(delete("/api/ProfileService/profile/{id}", profileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}
