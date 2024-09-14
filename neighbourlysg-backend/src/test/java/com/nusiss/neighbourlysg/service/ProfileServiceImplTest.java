package com.nusiss.neighbourlysg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.exception.PasswordWrongException;
import com.nusiss.neighbourlysg.exception.UserNotExistedException;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.exception.EmailInUseException;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.impl.ProfileServiceImpl;

import javax.management.relation.RoleNotFoundException;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class ProfileServiceImplTest {

	@Mock
	ProfileRepository profileRepository;
	@Autowired
	ProfileMapper profileMapper;
	@Mock
	RoleRepository roleRepository;

	private ProfileService profileService;
	
	@SuppressWarnings("deprecation")
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		profileService = new ProfileServiceImpl(profileRepository, profileMapper,roleRepository);
	}
	
	@Test
	void createProfileSuccess() throws RoleNotFoundException {
		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(roleRepository.findById(Mockito.any())).thenReturn(Optional.of(MasterEntityTestUtil.createRoleEntity()));
		Mockito.when(profileRepository.save(Mockito.any())).thenReturn(MasterEntityTestUtil.createProfileEntity());
		final ProfileDto dto = profileMapper.toDto(MasterEntityTestUtil.createProfileEntity());
		ProfileDto result = profileService.createProfile(MasterDTOTestUtil.createProfileDTO());
		assertEquals(dto.getId(), result.getId());
    }

	@Test
	void createProfileEmailInUse() {
		// Setup mock to return a profile when searching by email
		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));

		// Assert that creating a profile with an email in use throws an EmailInUseException
		assertThrows(EmailInUseException.class, () -> profileService.createProfile(MasterDTOTestUtil.createProfileDTO()));
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
		// Setup mock to return an empty Optional when searching by email
		Mockito.when(profileRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());

		// Assert that logging in with a non-existent user throws UserNotExistedException
		assertThrows(UserNotExistedException.class, () -> profileService.login(MasterDTOTestUtil.createLoginRequestDTO()));
	}
}
