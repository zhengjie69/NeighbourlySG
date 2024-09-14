package com.nusiss.neighbourlysg.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.RoleDto;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.exception.*;
import com.nusiss.neighbourlysg.mapper.RoleMapper;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import com.nusiss.neighbourlysg.service.impl.RoleServiceImpl;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.relation.RoleNotFoundException;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepository;

    @Autowired
    RoleMapper roleMapper;

    private RoleService roleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleServiceImpl(roleRepository, roleMapper);
    }

    @Test
    void createRole_ShouldCreateRoleWhenNotExists() {
        // Given
        RoleDto roleDto = MasterDTOTestUtil.createRoleDTO();

        when(roleRepository.findByName(roleDto.getName())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(roleMapper.toEntity(roleDto));

        // When
        RoleDto result = roleService.createRole(roleDto);

        // Then
        assertEquals(roleDto.getName(), result.getName());
    }

    @Test
    void createRole_RoleAlreadyExists() {
        // Given
        RoleDto roleDto = MasterDTOTestUtil.createRoleDTO();

        when(roleRepository.findByName(roleDto.getName())).thenReturn(Optional.ofNullable(roleMapper.toEntity(roleDto)));

        // When
        RoleAlreadyExistsException thrown = assertThrows(RoleAlreadyExistsException.class, () -> {
            roleService.createRole(roleDto);
        });

        // Then
        assertEquals("Role with name USER already exists", thrown.getMessage());
    }

    @Test
    void getAllRoles_ShouldReturnRoleDtos() {
        // Given
        Role role1 = new Role(); // Populate with test data
        role1.setId(1);
        role1.setName("test1");

        Role role2 = new Role(); // Populate with test data
        role2.setId(2);
        role2.setName("test2");

        RoleDto roleDto1 = new RoleDto(); // Populate with test data
        roleDto1.setId(1);
        roleDto1.setName("test1");

        RoleDto roleDto2 = new RoleDto(); // Populate with test data
        roleDto2.setId(2);
        roleDto2.setName("test2");

        List<Role> roles = List.of(role1, role2);
        List<RoleDto> expectedRoleDtos = List.of(roleDto1, roleDto2);

        when(roleRepository.findAll()).thenReturn(roles);

        // When
        List<RoleDto> result = roleService.getAllRoles();

        // Then
        assertEquals(expectedRoleDtos.get(0).getId(), result.get(0).getId());
        assertEquals(expectedRoleDtos.get(1).getId(), result.get(1).getId());
        verify(roleRepository).findAll();
    }

    @Test
    void getRoleById_ShouldReturnRoleDtoWhenRoleExists() throws RoleNotFoundException {
        // Given
        Role role = MasterEntityTestUtil.createRoleEntity(); // Populate with test data
        RoleDto roleDto = roleMapper.toDto(role); // Populate with test data

        when(roleRepository.findById(any())).thenReturn(Optional.of(role));

        // When
        RoleDto result = roleService.getRoleById(role.getId());

        // Then
        assertEquals(roleDto.getId(), result.getId());
    }

    @Test
    void updateRole_ShouldUpdateRoleWhenRoleExists() throws RoleNotFoundException {
        // Given
        Integer roleId = 1;
        Role existingRole = new Role(); // Populate with test data
        existingRole.setName("OldName");
        RoleDto roleDto = new RoleDto();
        roleDto.setName("NewName");

        Role updatedRole = new Role(); // Populate with test data
        updatedRole.setName("NewName");
        RoleDto updatedRoleDto = new RoleDto(); // Populate with test data
        updatedRoleDto.setName("NewName");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(existingRole)).thenReturn(updatedRole);

        // When
        RoleDto result = roleService.updateRole(roleId, roleDto);

        // Then
        assertEquals(updatedRoleDto.getId(), result.getId());
        verify(roleRepository).findById(roleId);
        verify(roleRepository).save(existingRole);
    }

    @Test
    void updateRole_ShouldThrowRoleNotFoundExceptionWhenRoleDoesNotExist() {
        // Given
        Integer roleId = 1;
        RoleDto roleDto = new RoleDto();
        roleDto.setName("NewName");

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // When & Then
        RoleNotFoundException thrown = assertThrows(RoleNotFoundException.class, () -> {
            roleService.updateRole(roleId, roleDto);
        });

        assertEquals("Role not found with id: " + roleId, thrown.getMessage());
    }
}
