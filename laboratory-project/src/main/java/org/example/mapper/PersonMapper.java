package org.example.mapper;

import org.example.constant.GlobalConst;
import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = GlobalConst.COMPONENT_MODEL, uses = {RoleMapper.class})
public interface PersonMapper {
    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonDto personDto);

    PersonDto toDto(Person person);

    @Mapping(target = "role", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Person update(@MappingTarget Person existingPerson, PersonDto newPersonDto);
}
