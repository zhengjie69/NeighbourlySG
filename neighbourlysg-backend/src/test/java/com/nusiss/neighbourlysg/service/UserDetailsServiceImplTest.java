package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class UserDetailsServiceImplTest {

    private ProfileRepository profileRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        profileRepository = Mockito.mock(ProfileRepository.class);
        userDetailsService = new UserDetailsServiceImpl(profileRepository);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        // Given
        String username = "testUser";
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setId(1L); // Add other necessary fields if required
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(profile));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        verify(profileRepository).findByUsername(username); // Verify that the repository method was called
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        // Given
        String username = "unknownUser";
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        assertEquals("User Not Found with username: " + username, exception.getMessage());
        verify(profileRepository).findByUsername(username); // Verify that the repository method was called
    }
}
