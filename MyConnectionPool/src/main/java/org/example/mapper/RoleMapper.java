package org.example.mapper;

import org.example.dto.RoleDto;
import org.example.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "default")
public interface RoleMapper {

    Role toEntity(RoleDto roleDto);

    RoleDto toDto(Role role);
}
