package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
public class ProfileControllerTest {
    @Mock
    ProfileService profileService;
    @Mock
    ProfileRepository profileRepository;

    @Autowired
    ProfileMapper profileMapper;

    private MockMvc mockMvc;

    private ProfileController profileController;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        profileController = new ProfileController(profileService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void createProfileTest() throws Exception {
        ProfileDto profileDto=MasterDTOTestUtil.createProfileDTO();
        Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(profileService.createProfile(Mockito.any())).thenReturn(profileDto);

        byte[] data = TestUtil.convertObjectToJsonBytes(profileDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + "/ProfileService/register")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8).content(data)).andExpect(status().isOk());
    }

    @Test
    void getProfileTest() throws Exception {
        ProfileDto profileDto=MasterDTOTestUtil.createProfileDTO();

        Mockito.when(profileService.getProfileById(Mockito.any())).thenReturn(profileDto);

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(profileDto);

        mockMvc .perform(get("/api/" + "/ProfileService/profile/{id}", profileDto.getId()).contentType(TestUtil.APPLICATION_JSON_UTF8).content(objectToJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(profileDto.getId()))
                .andExpect(jsonPath("$.email").value(profileDto.getEmail()))
                .andExpect(jsonPath("$.constituency").value(profileDto.getConstituency()))
                .andExpect(jsonPath("$.name").value(profileDto.getName()));
    }


}