package com.nusiss.neighbourlysg.security.jwt;

import com.nusiss.neighbourlysg.service.impl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    private JwtUtils jwtUtils;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();

    }

   @Test
    void testValidateJwtToken_malformed() {
        String malformedToken = "this.is.a.malformed.token";

        // Validate the malformed token
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }


    @Test
    void testValidateJwtToken_emptyToken() {
        String emptyToken = "";

        // Validate the empty token
        assertFalse(jwtUtils.validateJwtToken(emptyToken));
    }
}
