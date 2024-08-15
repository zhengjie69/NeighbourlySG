package com.nusiss.neighbourlysg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.controller.ProfileController;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.exception.PasswordWrongException;
import com.nusiss.neighbourlysg.exception.UserNotExistedException;
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

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.EmailInUseException;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.impl.ProfileServiceImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class ProfileServiceImplTest {

	@Mock
	ProfileRepository profileRepository;
	@Autowired
	ProfileMapper profileMapper;

	private ProfileService profileService;
	
	@SuppressWarnings("deprecation")
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		profileService = new ProfileServiceImpl(profileRepository, profileMapper);
	}
	
	@Test
	void createProfileSuccess() {
		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(profileRepository.save(Mockito.any())).thenReturn(MasterEntityTestUtil.createProfileEntity());
		final ProfileDto dto = profileMapper.toDto(MasterEntityTestUtil.createProfileEntity());
		ProfileDto result = profileService.createProfile(MasterDTOTestUtil.createProfileDTO());
		assertEquals(dto.getId(), result.getId());
    }
	
	@Test
	void createProfileEmailInUse() {
		//email in use
		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));
		//receive exception
		assertThrows(EmailInUseException.class, () -> {
			profileService.createProfile(MasterDTOTestUtil.createProfileDTO());
		});
    }

	@Test
	void loginSuccess() {
		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));
		final ProfileDto dto = profileMapper.toDto(MasterEntityTestUtil.createProfileEntity());
		ProfileDto result = profileService.login(MasterDTOTestUtil.createLoginRequestDTO());
		assertEquals(dto.getId(), result.getId());
	}


	@Test
	void loginFailedWrongPassWord() {

		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));
		final ProfileDto dto = profileMapper.toDto(MasterEntityTestUtil.createProfileEntity());

		//wrong password
		LoginRequestDTO loginRequestDTO = MasterDTOTestUtil.createLoginRequestDTO();
		loginRequestDTO.setPassword("wrongPassword");

		//receive exception
		assertThrows(PasswordWrongException.class, () -> {
			profileService.login(loginRequestDTO);
		});
	}

	@Test
	void loginFailedUserNotExisted() {

		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());

		//receive exception
		assertThrows(UserNotExistedException.class, () -> {
			profileService.login(MasterDTOTestUtil.createLoginRequestDTO());
		});
	}
}
