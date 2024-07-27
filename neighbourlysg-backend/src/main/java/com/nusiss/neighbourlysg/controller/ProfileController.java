package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private ProfileService profileService;

    //Create Profile REST API
    @PostMapping
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto) {
        ProfileDto savedProfile = profileService.createProfile(profileDto);
        return new ResponseEntity<>(savedProfile, HttpStatus.CREATED);
    }

    //Get Profile By Id REST API
    @GetMapping("{id}")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable("id") Long id) {
        ProfileDto retrievedProfile = profileService.getProfileById(id);
        return ResponseEntity.ok(retrievedProfile);
    }

    //Get All Profile REST API
    @GetMapping
    public ResponseEntity<List<ProfileDto>> getAllProfile() {
        List<ProfileDto> listOfRetrievedProfile = profileService.getAllProfile();
        return ResponseEntity.ok(listOfRetrievedProfile);
    }

    //Update Profile REST API
    @PutMapping("{id}")
    public ResponseEntity<ProfileDto> updateProfile(@PathVariable("id") Long id,
                                                    @RequestBody ProfileDto updatedProfile) {
        ProfileDto profileDto = profileService.updateProfile(id, updatedProfile);
        return ResponseEntity.ok(profileDto);
    }

    //Delete Profile REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable("id") Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.ok("Profile deleted successfully!");
    }
}
