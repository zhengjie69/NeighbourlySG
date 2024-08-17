package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.RoleDto;
import com.nusiss.neighbourlysg.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);
}
