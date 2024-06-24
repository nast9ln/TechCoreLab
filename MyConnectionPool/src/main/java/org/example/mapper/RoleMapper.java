package org.example.mapper;

import org.example.constant.GlobalConst;
import org.example.dto.RoleDto;
import org.example.entity.Role;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = GlobalConst.COMPONENT_MODEL)
public interface RoleMapper {

    Role toEntity(RoleDto roleDto);

    RoleDto toDto(Role role);
}
