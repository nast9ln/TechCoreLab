package org.example.service;

import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.example.entity.Role;
import org.example.enums.RoleEnum;
import org.example.exception.EntityNotFoundException;
import org.example.exception.LoginDuplicateException;
import org.example.mapper.PersonMapper;
import org.example.repository.PersonRepository;
import org.example.repository.RoleRepository;
import org.example.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPerson() {
        PersonDto personDto = new PersonDto();
        personDto.setLogin("user1");
        personDto.setPassword("password");

        Person person = new Person();
        person.setLogin("user1");
        person.setPassword("encodedPassword");
        person.setRegistrationDate(Instant.now());
        person.setRole(new Role(RoleEnum.ROLE_USER));

        when(personRepository.existsByLogin(personDto.getLogin())).thenReturn(false);
        when(personMapper.toEntity(personDto)).thenReturn(person);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(new Role(RoleEnum.ROLE_USER)));
        when(passwordEncoder.encode(personDto.getPassword())).thenReturn("encodedPassword");
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toDto(person)).thenReturn(personDto);

        PersonDto result = personService.create(personDto);

        assertNotNull(result);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void createPersonWithDuplicateLogin() {
        PersonDto personDto = new PersonDto();
        personDto.setLogin("user1");

        when(personRepository.existsByLogin(personDto.getLogin())).thenReturn(true);

        assertThrows(LoginDuplicateException.class, () -> personService.create(personDto));
    }

    @Test
    void updatePerson() {
        PersonDto personDto = new PersonDto();
        personDto.setId(1L);
        personDto.setLogin("user1");
        personDto.setPassword("newPassword");

        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName(RoleEnum.ROLE_USER);

        Person existingPerson = new Person();
        existingPerson.setId(1L);
        existingPerson.setLogin("user1");
        existingPerson.setPassword("oldPassword");
        existingPerson.setRole(existingRole);

        Role updatedRole = new Role();
        updatedRole.setId(1L);
        updatedRole.setName(RoleEnum.ROLE_USER);

        Person updatedPerson = new Person();
        updatedPerson.setId(1L);
        updatedPerson.setLogin("user1");
        updatedPerson.setPassword("encodedNewPassword");
        updatedPerson.setRole(updatedRole);

        when(personRepository.findById(personDto.getId())).thenReturn(Optional.of(existingPerson));
        when(personMapper.update(existingPerson, personDto)).thenReturn(updatedPerson);
        when(roleRepository.findById(updatedPerson.getRole().getId())).thenReturn(Optional.of(updatedRole));
        when(passwordEncoder.encode(personDto.getPassword())).thenReturn("encodedNewPassword");
        when(personRepository.save(updatedPerson)).thenReturn(updatedPerson);
        when(personMapper.toDto(updatedPerson)).thenReturn(personDto);

        PersonDto result = personService.update(personDto);

        assertNotNull(result);
        verify(personRepository, times(1)).save(updatedPerson);
    }


    @Test
    void readPerson() {
        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);

        PersonDto personDto = new PersonDto();
        personDto.setId(personId);

        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(personMapper.toDto(person)).thenReturn(personDto);

        PersonDto result = personService.read(personId);

        assertNotNull(result);
        verify(personRepository, times(1)).findById(personId);
    }

    @Test
    void readPersonNotFound() {
        Long personId = 1L;

        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personService.read(personId));
    }

    @Test
    void deletePerson() {
        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);

        when(personRepository.findById(personId)).thenReturn(Optional.of(person));

        personService.delete(personId);

        verify(personRepository, times(1)).delete(person);
    }

    @Test
    void deletePersonNotFound() {
        Long personId = 1L;

        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personService.delete(personId));
    }
}
