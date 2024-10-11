package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.service.impl.UserDetailsImpl;
import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class UserDetailsImplTest {

    private UserDetailsImpl userDetails;
    private Long id = 1L;
    private String username = "testUser";
    private String email = "test@example.com";
    private String password = "password";
    private Collection<GrantedAuthority> authorities;

    @BeforeEach
    void setUp() {
        authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        userDetails = new UserDetailsImpl(id, username, email, password, authorities);
    }

    @Test
    void testGetId() {
        assertEquals(id, userDetails.getId());
    }

    @Test
    void testGetUsername() {
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testGetEmail() {
        assertEquals(email, userDetails.getEmail());
    }

    @Test
    void testGetPassword() {
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> auths = userDetails.getAuthorities();
        assertEquals(1, auths.size());
        assertTrue(auths.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        UserDetailsImpl userDetails2 = new UserDetailsImpl(id, username, email, password, authorities);
        assertEquals(userDetails, userDetails2);
        assertEquals(userDetails.hashCode(), userDetails2.hashCode());

        UserDetailsImpl userDetails3 = new UserDetailsImpl(2L, "anotherUser", email, password, authorities);
        assertNotEquals(userDetails, userDetails3);
    }

    @Test
    void testBuild() {
        Profile profile = Mockito.mock(Profile.class);
        Mockito.when(profile.getId()).thenReturn(id);
        Mockito.when(profile.getUsername()).thenReturn(username);
        Mockito.when(profile.getEmail()).thenReturn(email);
        Mockito.when(profile.getPassword()).thenReturn(password);
        Set<Role> roles = new HashSet<>();
        roles.add(MasterEntityTestUtil.createRoleEntity());
        Mockito.when(profile.getRoles()).thenReturn(roles);

        UserDetailsImpl userDetailsFromProfile = UserDetailsImpl.build(profile);
        assertEquals(userDetails.getId(), userDetailsFromProfile.getId());
        assertEquals(userDetails.getUsername(), userDetailsFromProfile.getUsername());
        assertEquals(userDetails.getEmail(), userDetailsFromProfile.getEmail());
        assertEquals(userDetails.getPassword(), userDetailsFromProfile.getPassword());
    }
}