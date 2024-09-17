package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToRoleIds")
    ProfileDto toDto(Profile profile);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "roleIdsToRoles")
    Profile toEntity(ProfileDto profileDto);

    @Named("rolesToRoleIds")
    default List<Integer> rolesToRoleIds(List<Role> roles) {
        if (roles == null) {
            // Default to a list with ID 1 if roles is null
            return Collections.singletonList(1);
        }

        return roles.stream()
                .map(Role::getId)
                .toList();
    }

    @Named("roleIdsToRoles")
    default List<Role> roleIdsToRoles(List<Integer> roleIds) {
        if (roleIds == null) {
            // Default to a list with the Role having ID 1 if roleIds is null
            return Collections.singletonList(findRoleById(1));
        }

        return roleIds.stream()
                .map(this::findRoleById)
                .toList();
    }

    // Note: This method is not implemented in the interface.
    // Implementation will be in ProfileMapperImpl
    Role findRoleById(Integer id);
}