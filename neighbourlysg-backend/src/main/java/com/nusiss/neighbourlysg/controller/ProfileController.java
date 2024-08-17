package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;

import javax.management.relation.RoleNotFoundException;


@RestController
@RequestMapping("/api/ProfileService")
public class ProfileController {

    
    
	private final ProfileService profileService;
    
    public ProfileController(ProfileService profileService) {
    	this.profileService=profileService;
    	
    }

    //Create Profile REST API
    @PostMapping("/register")
    public ResponseEntity<?> createProfile(@RequestBody ProfileDto profileDto) {
        try {
            ProfileDto profile = profileService.createProfile(profileDto);
            return ResponseEntity.ok(profile);
        } catch (RoleNotFoundException e) {
            // Return a response with a 400 Bad Request status and error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role not found: " + e.getMessage());
        }
    }

    @PutMapping("/updateProfile/{id}")
    public ResponseEntity<ProfileDto> updateProfile(
            @PathVariable("id") Long id,
            @RequestBody ProfileDto updatedProfile) {

        try {
            ProfileDto profileDto = profileService.updateProfile(id, updatedProfile);
            return ResponseEntity.ok(profileDto);
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Handle other exceptions as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
