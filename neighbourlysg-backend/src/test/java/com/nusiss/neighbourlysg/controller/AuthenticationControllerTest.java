package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.JwtResponse;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.security.jwt.JwtUtils;
import com.nusiss.neighbourlysg.service.impl.UserDetailsImpl;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ProfileService profileService;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "test@example.com",
               "password",Collections.emptyList() );
        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testLoginSuccess() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "password");

            String password = loginRequest.getPassword();
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockJwtToken");

            ResponseEntity<JwtResponse> response = authenticationController.login(loginRequest);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            JwtResponse jwtResponse = response.getBody();
            assert jwtResponse != null;
            assertEquals("mockJwtToken", jwtResponse.getAccessToken());
            assertEquals(1L, jwtResponse.getId());
            assertEquals("testuser", jwtResponse.getUsername());
            assertEquals("test@example.com", jwtResponse.getEmail());

    }

    @Test
    void testCreateProfileSuccess() throws Exception {
        ProfileDto profileDto = new ProfileDto();
        // set profileDto properties as needed
        when(profileService.createProfile(any(ProfileDto.class))).thenReturn(profileDto);

        ResponseEntity<ProfileDto> response = authenticationController.createProfile(profileDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDto, response.getBody());
    }

    @Test
    void testCreateProfileRoleNotFound() throws Exception {
        ProfileDto profileDto = new ProfileDto();
        // set profileDto properties as needed
        when(profileService.createProfile(any(ProfileDto.class))).thenThrow(new RoleNotFoundException("Role not found"));

        ResponseEntity<ProfileDto> response = authenticationController.createProfile(profileDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
