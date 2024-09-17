package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.RoleDto;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto getRoleById(Integer id) throws RoleNotFoundException;
    List<RoleDto> getAllRoles();
    RoleDto updateRole(Integer id, RoleDto roleDto) throws RoleNotFoundException;
}