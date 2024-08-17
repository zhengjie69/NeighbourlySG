package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.RoleDto;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.exception.RoleAlreadyExistsException;
import com.nusiss.neighbourlysg.mapper.RoleMapper;
import com.nusiss.neighbourlysg.repository.RoleRepository;
import com.nusiss.neighbourlysg.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        // Check if the role already exists
        if (roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new RoleAlreadyExistsException("Role with name " + roleDto.getName() + " already exists");
        }

        Role role = roleMapper.toEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    public RoleDto getRoleById(Integer id) throws RoleNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        return roleMapper.toDto(role);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return List.of();
    }

    @Override
    @Transactional
    public RoleDto updateRole(Integer id, RoleDto roleDto) throws RoleNotFoundException {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));

        // Update fields if they are present in the DTO
        if (roleDto.getName() != null && !roleDto.getName().equals(existingRole.getName())) {
            existingRole.setName(roleDto.getName());
        }

        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toDto(updatedRole);
    }

    public Role findRoleById(Integer id) throws RoleNotFoundException {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role with ID " + id + " not found"));
    }
}