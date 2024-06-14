package org.example.mapper;

import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", uses = {RoleMapper.class})
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(target = "id", ignore = true)
    Person create(PersonDto personDto);

    Person toEntity(PersonDto personDto);

    PersonDto toDto(Person person);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Person update(@MappingTarget Person existingPerson, PersonDto newPersonDto);
}
