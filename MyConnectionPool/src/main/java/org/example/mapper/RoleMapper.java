package org.example.mapper;

import org.example.dto.RoleDto;
import org.example.entity.Role;

public class RoleMapper {
    public Role toEntity(RoleDto roleDto) {
        return Role.builder()
                .id(roleDto.getId())
                .name(roleDto.getName())
                .build();
    }

    public RoleDto toDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
