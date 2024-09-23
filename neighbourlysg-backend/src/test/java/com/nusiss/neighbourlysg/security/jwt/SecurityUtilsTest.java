package com.nusiss.neighbourlysg.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityUtilsTest {

    @BeforeEach
    void setUp() {
        // Clear the SecurityContext before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUserWhenAuthenticated() {
        // Arrange
        String expectedUsername = "testUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(expectedUsername);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Act
        String actualUsername = SecurityUtils.getCurrentUser();

        // Assert
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testGetCurrentUserWhenAnonymous() {
        // Arrange
        SecurityContextHolder.clearContext(); // No authentication set

        // Act
        String actualUsername = SecurityUtils.getCurrentUser();

        // Assert
        assertEquals("Anonymous", actualUsername);
    }

    @Test
    void testGetCurrentUserWhenAuthenticationIsNull() {
        // Arrange
        Authentication authentication = null;
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Act
        String actualUsername = SecurityUtils.getCurrentUser();

        // Assert
        assertEquals("Anonymous", actualUsername);
    }

    @Test
    void testGetCurrentUserWhenPrincipalIsNotUserDetails() {
        // Arrange
        Object nonUserDetailsPrincipal = new Object();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(nonUserDetailsPrincipal);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Act
        String actualUsername = SecurityUtils.getCurrentUser();

        // Assert
        assertEquals("Anonymous", actualUsername);
    }
}
