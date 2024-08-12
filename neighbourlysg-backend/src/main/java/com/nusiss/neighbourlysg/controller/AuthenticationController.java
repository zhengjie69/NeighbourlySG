package com.nusiss.neighbourlysg.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.service.ProfileService;

@RestController
public class AuthenticationController {

    
  
    private final ProfileService profileService;
    
    public AuthenticationController(ProfileService profileService) {
    	
    	this.profileService=profileService;
    }
    
	@PostMapping("/login")
    public ResponseEntity<ProfileDto> login(@RequestBody LoginRequestDTO loginRequest) {
        ProfileDto profile = profileService.login(loginRequest);
        return ResponseEntity.ok(profile);
    }
}
