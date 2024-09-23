package com.nusiss.neighbourlysg.controller;
import com.nusiss.neighbourlysg.dto.JwtResponse;
import com.nusiss.neighbourlysg.security.jwt.JwtUtils;
import com.nusiss.neighbourlysg.security.services.UserDetailsImpl;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;

import javax.management.relation.RoleNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final ProfileService profileService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, ProfileService profileService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.profileService = profileService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    //Create Profile REST API
    @PostMapping("/register")
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto) {
        try {
            ProfileDto profile = profileService.createProfile(profileDto);
            return ResponseEntity.ok(profile);
        } catch (RoleNotFoundException e) {
            // Return a response with a 400 Bad Request status and error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



}
