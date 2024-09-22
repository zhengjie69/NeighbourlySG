package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.RoleAssignmentDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.exception.EmailInUseException;
import com.nusiss.neighbourlysg.exception.PasswordWrongException;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.exception.UserNotExistedException;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import com.nusiss.neighbourlysg.service.impl.ProfileServiceImpl;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.RoleNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class ProfileServiceImplTest {

	@Mock
	ProfileRepository profileRepository;
	@Autowired
	ProfileMapper profileMapper;
	@Mock
	RoleRepository roleRepository;
	@Mock
	PasswordEncoder encoder;

	private ProfileService profileService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		profileService = new ProfileServiceImpl(profileRepository, profileMapper,roleRepository,encoder);
	}
	
	@Test
	void createProfileSuccessWithoutRole() throws RoleNotFoundException {
		when(profileRepository.findByEmail(any())).thenReturn(Optional.empty());
		when(roleRepository.findByName(any())).thenReturn(Optional.of(MasterEntityTestUtil.createRoleEntity()));
		when(profileRepository.save(any())).thenReturn(MasterEntityTestUtil.createProfileEntity());
		final ProfileDto dto = MasterDTOTestUtil.createProfileDTO();
		ProfileDto result = profileService.createProfile(MasterDTOTestUtil.createProfileDTO());
		assertEquals(dto.getId(), result.getId());
    }

	@Test
	void createProfileSuccessWithRole() throws RoleNotFoundException {
		when(profileRepository.findByEmail(any())).thenReturn(Optional.empty());
		when(roleRepository.findByName(any())).thenReturn(Optional.of(MasterEntityTestUtil.createRoleEntity()));
		when(profileRepository.save(any())).thenReturn(MasterEntityTestUtil.createProfileEntity());
		final ProfileDto dto = MasterDTOTestUtil.createProfileDTO();
		ProfileDto result = profileService.createProfile(MasterDTOTestUtil.createProfileDTOWithRoles());
		assertEquals(dto.getId(), result.getId());
	}

	@Test
	void createProfileEmailInUse() {
		// Setup mock to return a profile when searching by email
		when(profileRepository.findByEmail(any())).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));

		// Assert that creating a profile with an email in use throws an EmailInUseException
		assertThrows(EmailInUseException.class, () -> {
			// Only the following invocation is allowed in the lambda
			profileService.createProfile(MasterDTOTestUtil.createProfileDTO());
		});
	}

	@Test
	void getProfileById_ShouldReturnProfileDtoWhenProfileExists() {
		// Given
		Long profileId = 1L;
		Profile profile = MasterEntityTestUtil.createProfileEntity(); // Populate this with test data
		ProfileDto profileDto = MasterDTOTestUtil.createProfileDTO(); // Populate this with test data

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

		// When
		ProfileDto result = profileService.getProfileById(profileId);

		// Then
		assertEquals(profileDto.getId(), result.getId());
	}

	@Test
	void getProfileById_ShouldThrowIllegalArgumentExceptionWhenIdIsNull() {

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			profileService.getProfileById(null);
		});

		assertEquals("No Profile Id is inputted", thrown.getMessage());
	}

	@Test
	void getProfileById_ShouldThrowProfileNotFoundExceptionWhenProfileDoesNotExist() {
		// Given
		Long profileId = 1L;

		when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

		// When & Then
		ProfileNotFoundException thrown = assertThrows(ProfileNotFoundException.class, () -> {
			profileService.getProfileById(profileId);
		});

		assertEquals("Profile not found with id: " + profileId, thrown.getMessage());
	}

	@Test
	void updateProfile_ShouldUpdateAllFieldsWhenAllProvided() throws RoleNotFoundException {
		// Given
		Role newRole = MasterEntityTestUtil.createRoleEntity();
		newRole.setId(2);
		newRole.setName("newRole");

		Set<Role> roles = new HashSet<>();
		roles.add(MasterEntityTestUtil.createRoleEntity());

		Profile existingProfile = MasterEntityTestUtil.createProfileEntity();
		existingProfile.setName("test2");
		existingProfile.setEmail("test2Email");
		existingProfile.setConstituency("testConstituency");
		existingProfile.setPassword("testPassword");
		existingProfile.setRoles(roles);

		Profile updatedProfile = new Profile();
		updatedProfile.setId(1L);
		updatedProfile.setName("test2");
		updatedProfile.setEmail("test2Email");
		updatedProfile.setConstituency("testConstituency");
		updatedProfile.setPassword("testPassword");
		updatedProfile.setRoles(roles);

		ProfileDto profileDto = MasterDTOTestUtil.createProfileDTOWithRoles();

		when(roleRepository.findByName(any())).thenReturn(Optional.of(MasterEntityTestUtil.createRoleEntity()));
		when(profileRepository.findById(any())).thenReturn(Optional.of(existingProfile));
		when(profileRepository.save(existingProfile)).thenReturn(updatedProfile);

		// When
		ProfileDto result = profileService.updateProfile(existingProfile.getId(), profileDto);

		// Then
		assertEquals(profileDto.getId(), result.getId());
		verify(profileRepository).save(existingProfile);
	}

	@Test
	void deleteProfile_ShouldDeleteProfileWhenExists() {
		// Given
		Long profileId = 1L;
		Profile profile = new Profile(); // Populate with test data
		profile.setId(profileId);

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

		// When
		profileService.deleteProfile(profileId);

		// Then
		verify(profileRepository).delete(profile); // Verify that delete() is called with the correct profile
	}

	@Test
	void getAllProfiles_ShouldReturnListOfProfileDtos() {
		// Given
		Profile profile1 = new Profile(); // Populate with test data
		profile1.setId(1L);
		Profile profile2 = new Profile(); // Populate with test data
		profile2.setId(2L);
		ProfileDto profileDto1 = new ProfileDto(); // Populate with test data
		profileDto1.setId(1L);
		ProfileDto profileDto2 = new ProfileDto(); // Populate with test data
		profileDto2.setId(2L);

		List<Profile> profiles = Arrays.asList(profile1, profile2);
		List<ProfileDto> profileDtos = Arrays.asList(profileDto1, profileDto2);

		when(profileRepository.findAll()).thenReturn(profiles);

		// When
		List<ProfileDto> result = profileService.getAllProfiles();

		// Then
		assertEquals(profileDtos.get(0).getId(), result.get(0).getId());
		assertEquals(profileDtos.get(1).getId(), result.get(1).getId());
		verify(profileRepository).findAll(); // Ensure findAll was called
	}

	@Test
	void assignRoleToUser_ShouldAssignRolesWhenProfileAndRolesExist() throws RoleNotFoundException, ProfileNotFoundException {
		// Given
		Long profileId = 1L;
		Role role1 = MasterEntityTestUtil.createRoleEntity(); // Assume this creates a role with id 1
		Role role2 = new Role();
		role2.setId(2);
		role2.setName("NEW_ROLE");

		Profile profile = MasterEntityTestUtil.createProfileEntity(); // A profile with some roles
		Set<Role> roles = new HashSet<>();
		roles.add(role1);
		profile.setRoles(roles);

		RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
		roleAssignmentDto.setUserId(profileId);
		roleAssignmentDto.setRoleIds(Arrays.asList(1, 2)); // Request to assign two roles

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
		when(roleRepository.findById(1)).thenReturn(Optional.of(role1));
		when(roleRepository.findById(2)).thenReturn(Optional.of(role2));
		when(profileRepository.save(any())).thenReturn(profile);

		// When
		ProfileDto result = profileService.assignRoleToUser(roleAssignmentDto);

		// Then
		assertEquals(2, result.getRoles().size()); // Verify that two roles are assigned
		verify(profileRepository).save(profile); // Ensure the profile was saved with updated roles
	}

	@Test
	void isAdmin_ShouldReturnTrueIfProfileHasAdminRole() {
		// Given
		Long profileId = 1L;
		Role adminRole = new Role();
		adminRole.setId(3); // Admin role has ID 3
		Profile profile = MasterEntityTestUtil.createProfileEntity();
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);
		profile.setRoles(roles); // Profile with admin role

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

		// When
		boolean result = profileService.isAdmin(profileId);

		// Then
		assertTrue(result); // Ensure the profile is an admin
	}

	@Test
	void isAdmin_ShouldReturnFalseIfProfileDoesNotHaveAdminRole() {
		// Given
		Long profileId = 1L;
		Role nonAdminRole = new Role();
		nonAdminRole.setId(2); // Non-admin role
		Set roles = new HashSet<>();
		roles.add(nonAdminRole);
		Profile profile = MasterEntityTestUtil.createProfileEntity();
		profile.setRoles(roles); // Profile without admin role

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

		// When
		boolean result = profileService.isAdmin(profileId);

		// Then
		assertFalse(result); // Ensure the profile is not an admin
	}


	@Test
	void updateProfileProfileNotFound() {
		Long profileId = 1L;
		ProfileDto profileDto = MasterDTOTestUtil.createProfileDTO();

		when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

		assertThrows(ProfileNotFoundException.class, () -> profileService.updateProfile(profileId, profileDto));
	}


	@Test
	void updateRolesProfileNotFound() {
		Long profileId = 1L;
		List<Integer> roleIds = Arrays.asList(1, 2);

		when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

		assertThrows(ProfileNotFoundException.class, () -> profileService.updateRoles(profileId, roleIds));
	}

	@Test
	void updateRolesRoleNotFound() {
		Long profileId = 1L;
		List<Integer> roleIds = Arrays.asList(1, 2);

		Profile existingProfile = MasterEntityTestUtil.createProfileEntity();
		existingProfile.setRoles(Collections.emptySet());

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(existingProfile));
		when(roleRepository.findById(1)).thenReturn(Optional.empty()); // Role not found

		assertThrows(com.nusiss.neighbourlysg.exception.RoleNotFoundException.class, () -> profileService.updateRoles(profileId, roleIds));
	}

	@Test
	void assignRoleToUserProfileNotFound() {
		RoleAssignmentDto roleAssignmentDto = new RoleAssignmentDto();
		roleAssignmentDto.setUserId(1L);
		roleAssignmentDto.setRoleIds(Arrays.asList(1, 2));

		when(profileRepository.findById(roleAssignmentDto.getUserId())).thenReturn(Optional.empty());

		assertThrows(ProfileNotFoundException.class, () -> profileService.assignRoleToUser(roleAssignmentDto));
	}

	@Test
	void deleteProfileProfileNotFound() {
		Long profileId = 1L;

		when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

		assertThrows(ProfileNotFoundException.class, () -> profileService.deleteProfile(profileId));
	}

	@Test
	void getAllProfilesEmptyRepository() {
		when(profileRepository.findAll()).thenReturn(Collections.emptyList());

		List<ProfileDto> result = profileService.getAllProfiles();

		assertTrue(result.isEmpty()); // Ensure result is empty
	}

	@Test
	void updateRoles_ShouldUpdateRolesWhenProfileAndRolesExist() throws RoleNotFoundException, ProfileNotFoundException {
		// Given
		Long profileId = 1L;
		Role role1 = MasterEntityTestUtil.createRoleEntity();
		Role role2 = new Role();
		role2.setId(2);
		role2.setName("NEW_ROLE");

		Profile profile = MasterEntityTestUtil.createProfileEntity();
		Set<Role> roles = new HashSet<>();
		roles.add(role1);
		profile.setRoles(roles);

		HashSet<Role> newRoles = new HashSet<>();
		newRoles.add(role1);
		newRoles.add(role2);

		Profile updatedProfile = new Profile();
		updatedProfile.setId(profileId);
		updatedProfile.setRoles(newRoles);

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
		when(roleRepository.findById(1)).thenReturn(Optional.of(role1));
		when(roleRepository.findById(2)).thenReturn(Optional.of(role2));
		when(profileRepository.save(Mockito.any(Profile.class))).thenReturn(updatedProfile);

		// When
		ProfileDto result = profileService.updateRoles(profileId, Arrays.asList(1, 2));

		// Then
		assertEquals(2, result.getRoles().size());
		verify(profileRepository).save(Mockito.any(Profile.class));
	}


	@Test
	void findById_ShouldReturnProfileWhenProfileExists() {
		// Given
		Long profileId = 1L;
		Profile profile = MasterEntityTestUtil.createProfileEntity();

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

		// When
		Profile result = profileService.findById(profileId);

		// Then
		assertEquals(profile, result);
	}

	@Test
	void findById_ShouldThrowProfileNotFoundExceptionWhenProfileDoesNotExist() {
		// Given
		Long profileId = 1L;

		when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ProfileNotFoundException.class, () -> profileService.findById(profileId));
	}
}
