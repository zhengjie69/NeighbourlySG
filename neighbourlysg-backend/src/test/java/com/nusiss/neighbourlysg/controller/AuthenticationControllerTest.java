package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import com.nusiss.neighbourlysg.security.jwt.JwtUtils;
import com.nusiss.neighbourlysg.service.ProfileService;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
import com.nusiss.neighbourlysg.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class AuthenticationControllerTest {
    @Mock
    ProfileService profileService;
    @Mock
    ProfileRepository profileRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtUtils jwtUtils;


    private MockMvc mockMvc;

    private AuthenticationController authenticationController;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController( authenticationManager,  roleRepository,
                 encoder,  jwtUtils,  profileService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void loginTest() throws Exception {
        LoginRequestDTO loginRequestDTO=MasterDTOTestUtil.createLoginRequestDTO();
        Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));

        byte[] data = TestUtil.convertObjectToJsonBytes(loginRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).content(data)).andExpect(status().isOk());
    }
}
