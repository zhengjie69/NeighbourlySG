package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ProfileService")
public class ProfileController {

    
    
	private final ProfileService profileService;
    
    public ProfileController(ProfileService profileService) {
    	this.profileService=profileService;
    	
    }

    //Create Profile REST API
    @PostMapping("/register")
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto) {
        ProfileDto profile = profileService.createProfile(profileDto);
        return ResponseEntity.ok(profile);
    }
    

//    //Get Profile By Id REST API
//    @GetMapping("{id}")
//    public ResponseEntity<ProfileDto> getProfileById(@PathVariable("id") Long id) {
//        ProfileDto retrievedProfile = profileService.getProfileById(id);
//        return ResponseEntity.ok(retrievedProfile);
//    }
//
//    //Get All Profile REST API
//    @GetMapping
//    public ResponseEntity<List<ProfileDto>> getAllProfile() {
//        List<ProfileDto> listOfRetrievedProfile = profileService.getAllProfile();
//        return ResponseEntity.ok(listOfRetrievedProfile);
//    }
//
//    //Update Profile REST API
//    @PutMapping("{id}")
//    public ResponseEntity<ProfileDto> updateProfile(@PathVariable("id") Long id,
//                                                    @RequestBody ProfileDto updatedProfile) {
//        ProfileDto profileDto = profileService.updateProfile(id, updatedProfile);
//        return ResponseEntity.ok(profileDto);
//    }
//
//    //Delete Profile REST API
//    @DeleteMapping("{id}")
//    public ResponseEntity<String> deleteProfile(@PathVariable("id") Long id) {
//        profileService.deleteProfile(id);
//        return ResponseEntity.ok("Profile deleted successfully!");
//    }
}
