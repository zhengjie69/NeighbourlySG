//package com.nusiss.neighbourlysg.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
//import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
//import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
//import com.nusiss.neighbourlysg.entity.Profile;
//import com.nusiss.neighbourlysg.entity.Role;
//import com.nusiss.neighbourlysg.exception.PasswordWrongException;
//import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
//import com.nusiss.neighbourlysg.exception.UserNotExistedException;
//import com.nusiss.neighbourlysg.repository.RoleRepository;
//import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
//import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.nusiss.neighbourlysg.dto.ProfileDto;
//import com.nusiss.neighbourlysg.exception.EmailInUseException;
//import com.nusiss.neighbourlysg.mapper.ProfileMapper;
//import com.nusiss.neighbourlysg.repository.ProfileRepository;
//import com.nusiss.neighbourlysg.service.impl.ProfileServiceImpl;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import javax.management.relation.RoleNotFoundException;
//
//@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
//class ProfileServiceImplTest {
//
//	@Mock
//	ProfileRepository profileRepository;
//	@Autowired
//	ProfileMapper profileMapper;
//	@Mock
//	RoleRepository roleRepository;
//	@Mock
//	PasswordEncoder encoder;
//
//	private ProfileService profileService;
//
//	@BeforeEach
//	void setup() {
//		MockitoAnnotations.openMocks(this);
//		profileService = new ProfileServiceImpl(profileRepository, profileMapper,roleRepository,encoder);
//	}
//
//	@Test
//	void createProfileSuccess() throws RoleNotFoundException {
//		when(profileRepository.findByEmail(any())).thenReturn(Optional.empty());
//		when(roleRepository.findById(any())).thenReturn(Optional.of(MasterEntityTestUtil.createRoleEntity()));
//		when(profileRepository.save(any())).thenReturn(MasterEntityTestUtil.createProfileEntity());
//		final ProfileDto dto = profileMapper.toDto(MasterEntityTestUtil.createProfileEntity());
//		ProfileDto result = profileService.createProfile(MasterDTOTestUtil.createProfileDTO());
//		assertEquals(dto.getId(), result.getId());
//    }
//
//	@Test
//	void createProfileEmailInUse() {
//		// Setup mock to return a profile when searching by email
//		when(profileRepository.findByEmail(any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));
//
//		// Assert that creating a profile with an email in use throws an EmailInUseException
//		assertThrows(EmailInUseException.class, () -> {
//			// Only the following invocation is allowed in the lambda
//			profileService.createProfile(MasterDTOTestUtil.createProfileDTO());
//		});
//	}
//
//	@Test
//	void createProfileRoleEmpty() throws RoleNotFoundException {
//		when(profileRepository.findByEmail(any())).thenReturn(Optional.empty());
//
//		ProfileDto profileDtoWithoutRole = MasterDTOTestUtil.createProfileDTO();
//		profileDtoWithoutRole.setRoles(null);
//
//		Profile profileWithoutRole = MasterEntityTestUtil.createProfileEntity();
//		profileWithoutRole.setRoles(null);
//
//		when(profileRepository.save(any())).thenReturn(profileWithoutRole);
//
//		when(roleRepository.findByName(any())).thenReturn(Optional.of(MasterEntityTestUtil.createRoleEntity()));
//
//		final ProfileDto dto = profileMapper.toDto(MasterEntityTestUtil.createProfileEntity());
//		ProfileDto result = profileService.createProfile(profileDtoWithoutRole);
//		assertEquals(dto.getId(), result.getId());
//	}
//
//	@Test
//	void loginSuccess() {
//		when(profileRepository.findByEmail(any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));
//		final ProfileDto dto = profileMapper.toDto(MasterEntityTestUtil.createProfileEntity());
//		ProfileDto result = profileService.login(MasterDTOTestUtil.createLoginRequestDTO());
//		assertEquals(dto.getId(), result.getId());
//	}
//
//
//	@Test
//	void loginFailedWrongPassWord() {
//
//		when(profileRepository.findByEmail(any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));
//
//		//wrong password
//		LoginRequestDTO loginRequestDTO = MasterDTOTestUtil.createLoginRequestDTO();
//		loginRequestDTO.setPassword("wrongPassword");
//
//		//receive exception
//		assertThrows(PasswordWrongException.class, () -> {
//			profileService.login(loginRequestDTO);
//		});
//	}
//
//	@Test
//	void loginFailedUserNotExisted() {
//		// Setup mock to return an empty Optional when searching by email
//		when(profileRepository.findByEmail(any())).thenReturn(Optional.empty());
//
//		// Assert that logging in with a non-existent user throws UserNotExistedException
//		assertThrows(UserNotExistedException.class, () -> profileService.login(MasterDTOTestUtil.createLoginRequestDTO()));
//	}
//
//	@Test
//	void getProfileById_ShouldReturnProfileDtoWhenProfileExists() {
//		// Given
//		Long profileId = 1L;
//		Profile profile = MasterEntityTestUtil.createProfileEntity(); // Populate this with test data
//		ProfileDto profileDto = MasterDTOTestUtil.createProfileDTO(); // Populate this with test data
//
//		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
//
//		// When
//		ProfileDto result = profileService.getProfileById(profileId);
//
//		// Then
//		assertEquals(profileDto.getId(), result.getId());
//	}
//
//	@Test
//	void getProfileById_ShouldThrowIllegalArgumentExceptionWhenIdIsNull() {
//
//		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//			profileService.getProfileById(null);
//		});
//
//		assertEquals("No Profile Id is inputted", thrown.getMessage());
//	}
//
//	@Test
//	void getProfileById_ShouldThrowProfileNotFoundExceptionWhenProfileDoesNotExist() {
//		// Given
//		Long profileId = 1L;
//
//		when(profileRepository.findById(profileId)).thenReturn(Optional.empty());
//
//		// When & Then
//		ProfileNotFoundException thrown = assertThrows(ProfileNotFoundException.class, () -> {
//			profileService.getProfileById(profileId);
//		});
//
//		assertEquals("Profile not found with id: " + profileId, thrown.getMessage());
//	}
//
//	@Test
//	void updateProfile_ShouldUpdateAllFieldsWhenAllProvided() throws RoleNotFoundException {
//		// Given
//		Role newRole = MasterEntityTestUtil.createRoleEntity();
//		newRole.setId(2);
//		newRole.setName("newRole");
//		List<Role> roles = List.of(MasterEntityTestUtil.createRoleEntity(), newRole);
//
//		Profile existingProfile = MasterEntityTestUtil.createProfileEntity();
//		existingProfile.setName("test2");
//		existingProfile.setEmail("test2Email");
//		existingProfile.setConstituency("testConstituency");
//		existingProfile.setPassword("testPassword");
//		existingProfile.setRoles(roles);
//
//		Profile updatedProfile = new Profile();
//		updatedProfile.setId(1L);
//		updatedProfile.setName("test2");
//		updatedProfile.setEmail("test2Email");
//		updatedProfile.setConstituency("testConstituency");
//		updatedProfile.setPassword("testPassword");
//		updatedProfile.setRoles(roles);
//
//		ProfileDto profileDto = MasterDTOTestUtil.createProfileDTO();
//
//		when(roleRepository.findById(any())).thenReturn(Optional.of(MasterEntityTestUtil.createRoleEntity()));
//		when(profileRepository.findById(any())).thenReturn(Optional.of(existingProfile));
//		when(profileRepository.save(existingProfile)).thenReturn(updatedProfile);
//
//		// When
//		ProfileDto result = profileService.updateProfile(existingProfile.getId(), profileDto);
//
//		// Then
//		assertEquals(profileDto.getId(), result.getId());
//		verify(profileRepository).save(existingProfile);
//	}
//
//	@Test
//	void deleteProfile_ShouldDeleteProfileWhenExists() {
//		// Given
//		Long profileId = 1L;
//		Profile profile = new Profile(); // Populate with test data
//		profile.setId(profileId);
//
//		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
//
//		// When
//		profileService.deleteProfile(profileId);
//
//		// Then
//		verify(profileRepository).deleteById(profileId);
//	}
//
//	@Test
//	void getAllProfiles_ShouldReturnListOfProfileDtos() {
//		// Given
//		Profile profile1 = new Profile(); // Populate with test data
//		profile1.setId(1L);
//		Profile profile2 = new Profile(); // Populate with test data
//		profile2.setId(2L);
//		ProfileDto profileDto1 = new ProfileDto(); // Populate with test data
//		profileDto1.setId(1L);
//		ProfileDto profileDto2 = new ProfileDto(); // Populate with test data
//		profileDto2.setId(2L);
//
//		List<Profile> profiles = Arrays.asList(profile1, profile2);
//		List<ProfileDto> profileDtos = Arrays.asList(profileDto1, profileDto2);
//
//		when(profileRepository.findAll()).thenReturn(profiles);
//
//		// When
//		List<ProfileDto> result = profileService.getAllProfiles();
//
//		// Then
//		assertEquals(profileDtos.get(0).getId(), result.get(0).getId());
//		assertEquals(profileDtos.get(1).getId(), result.get(1).getId());
//		verify(profileRepository).findAll(); // Ensure findAll was called
//	}
//
//	@Test
//	void assignRoleToUser_ShouldAssignRoleWhenProfileAndRoleExist() throws RoleNotFoundException, ProfileNotFoundException {
//		// Given
//		Long userId = 1L;
//		int roleId = 2;
//
//		Role role = new Role(); // Populate with test data
//		Profile profile = new Profile();
//
//		RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
//		roleAssignmentDto.setUserId(userId);
//		roleAssignmentDto.setRoleId(roleId);
//
//		Profile updatedProfile = new Profile();
//		updatedProfile.setRoles(Collections.singletonList(MasterEntityTestUtil.createRoleEntity())); // After adding role
//
//		when(profileRepository.findById(userId)).thenReturn(Optional.of(profile));
//		when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
//		when(profileRepository.save(profile)).thenReturn(updatedProfile);
//
//		// When
//		ProfileDto result = profileService.assignRoleToUser(roleAssignmentDto);
//
//		// Then
//		assertNotNull(result);
//		verify(profileRepository).save(profile);
//	}
//}
