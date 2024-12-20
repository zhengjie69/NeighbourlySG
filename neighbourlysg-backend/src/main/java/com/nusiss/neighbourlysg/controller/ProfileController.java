package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/ProfileService")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;

    }

    // Get All Profiles/Users
    @GetMapping("/profiles")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        List<ProfileDto> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles); // Return the list of profiles with 200 OK status
    }

    // Get Profile By Id REST API
    @GetMapping("/profile/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable("id") Long id) {

        try {
            ProfileDto retrievedProfile = profileService.getProfileById(id);
            return ResponseEntity.ok(retrievedProfile);
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Handle other exceptions as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updateProfile/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
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

    @PutMapping("/assign-role")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<Object> assignRoleToUser(@RequestBody RoleAssignmentDto roleAssignmentDto) {
        try {
            ProfileDto updatedProfile = profileService.updateRoles(roleAssignmentDto.getUserId(),
                    roleAssignmentDto.getRoleIds());
            return ResponseEntity.ok(updatedProfile);
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role not found: " + e.getMessage());
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profile not found: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while updating roles");
        }
    }

    // Delete Profile REST API
    @DeleteMapping("/profile/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteProfile(@PathVariable("id") Long id) {
        try {
            profileService.deleteProfile(id);
            return ResponseEntity.ok("Profile deleted successfully!");
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Handle other exceptions as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
