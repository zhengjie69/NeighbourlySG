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
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRolesToStrRoles")
    ProfileDto toDto(Profile profile);

    @Mapping(target = "roles", ignore = true)
    Profile toEntity(ProfileDto profileDto);


    // Custom mapping for roles
    @Named("mapRolesToStrRoles")
    default Set<String> mapRolesToStrRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName) // Assuming Role has a getName() method
                .collect(Collectors.toSet());
    }
}