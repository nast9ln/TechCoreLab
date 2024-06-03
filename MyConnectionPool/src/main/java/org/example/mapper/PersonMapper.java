package org.example.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.PersonDto;
import org.example.entity.Person;

import java.io.IOException;
import java.io.Reader;

public class PersonMapper {
    private final RoleMapper roleMapper;
    private final ObjectMapper objectMapper;

    public PersonMapper(RoleMapper roleMapper, ObjectMapper objectMapper) {
        this.roleMapper = roleMapper;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public Person toEntity(PersonDto personDto) {
        return Person.builder()
                .id(personDto.getId())
                .name(personDto.getName())
                .login(personDto.getLogin())
                .registrationDate(personDto.getRegistrationDate())
                .role(personDto.getRole()==null ? null : roleMapper.toEntity(personDto.getRole()))
                .build();
    }

    public PersonDto toDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .login(person.getLogin())
                .registrationDate(person.getRegistrationDate())
                .role(roleMapper.toDto(person.getRole()))
                .build();
    }

    public String toJson(PersonDto personDto) {
        try {
            return objectMapper.writeValueAsString(personDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting PersonDto to JSON", e);
        }
    }


    public PersonDto fromJson(Reader reader) throws IOException {
        return objectMapper.readValue(reader, PersonDto.class);
    }
}
