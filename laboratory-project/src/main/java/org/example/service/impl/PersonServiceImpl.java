package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.security.JwtPerson;
import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.example.entity.Role;
import org.example.enums.RoleEnum;
import org.example.exception.EntityNotFoundException;
import org.example.exception.LoginDuplicateException;
import org.example.mapper.PersonMapper;
import org.example.repository.PersonRepository;
import org.example.repository.RoleRepository;
import org.example.service.security.JwtAuthorizationService;
import org.example.service.PersonService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthorizationService jwtAuthorizationService;


    @Override
    public PersonDto create(PersonDto personDto) {
        log.info("Creating person with data: {}", personDto);
        if (personRepository.existsByLogin(personDto.getLogin())) {
            log.error("Duplicate login detected for {}", personDto.getLogin());
            throw new LoginDuplicateException("Login already in use: " + personDto.getLogin());
        }
        Person person = personMapper.toEntity(personDto);
        person.setRole(checkAndSetRole(person));
        person.setRegistrationDate(Instant.now());
        person.setPassword(passwordEncoder.encode(personDto.getPassword()));
        PersonDto createdPersonDto = personMapper.toDto(personRepository.save(person));
        log.info("Person created with ID: {}", createdPersonDto.getId());
        return createdPersonDto;
    }

    public PersonDto update(PersonDto personDto) {
        Person existingPerson = personRepository.findById(personDto.getId())
                .orElseThrow(() -> {
                    log.error("Person with ID {} not found for update", personDto.getId());
                    return new EntityNotFoundException("Person with id {0} not found", personDto.getId());
                });


        Person newPerson = personMapper.toEntity(personDto);
        if (personDto.getPassword() != null && !personDto.getPassword().isEmpty()) {
            newPerson.setPassword(passwordEncoder.encode(personDto.getPassword()));
        }
        personRepository.save(personMapper.update(existingPerson, personDto));
        return personMapper.toDto(newPerson);
    }


    @Transactional(readOnly = true)
    @Override
    public PersonDto read(Long id) {
        log.info("Reading person with ID: {}", id);
        PersonDto personDto = personMapper.toDto(personRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Person with ID {} not found", id);
                    return new EntityNotFoundException("Person with id " + id + " not found");
                }));
        log.info("Person read with ID: {}", personDto.getId());
        return personDto;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting person with ID: {}", id);

        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Person with ID {} not found for delete", id);
                    return new EntityNotFoundException("Person with id {0} not found", id);
                });

        personRepository.delete(existingPerson);
        log.info("Person deleted with ID: {}", id);
    }

    @Override
    public void delete() {
        log.info("Deleting person with token");
        JwtPerson jwtPerson = jwtAuthorizationService.extractJwtPerson();
        Person existingPerson = personRepository.findById(jwtPerson.getId())
                .orElseThrow(() -> {
                    log.error("Person with ID {} not found for delete", jwtPerson.getId());
                    return new EntityNotFoundException("Person with id {0} not found", jwtPerson.getId());
                });
        personRepository.delete(existingPerson);
    }

    /**
     * Проверяет и устанавливает роль пользователя на основе переданных данных.
     *
     * @param person Объект Person, для которого необходимо проверить и установить роль.
     * @return Объект Role, представляющий роль пользователя.
     * @throws EntityNotFoundException Если не удалось найти соответствующую роль в базе данных.
     */
    private Role checkAndSetRole(Person person) {
        Role role = person.getRole();
        if (role != null) {
            Long roleId = role.getId();
            String roleName = String.valueOf(role.getName());

            if (roleId != null) {
                log.info("Checking and setting role with ID: {}", roleId);
                return roleRepository.findById(roleId)
                        .orElseThrow(() -> {
                            log.error("Role with ID {} not found", roleId);
                            return new EntityNotFoundException("Cannot find role with ID: " + roleId);
                        });
            } else if (roleName != null) {
                log.info("Checking and setting role with name: {}", roleName);
                return roleRepository.findByName(RoleEnum.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> {
                            log.error("Role with name {} not found", roleName);
                            return new EntityNotFoundException("Cannot find role with name: " + roleName);
                        });
            }
        }

        log.info("Role is null or not defined. Setting default role to USER.");
        return roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> {
                    log.error("Default role USER not found");
                    return new EntityNotFoundException("Cannot find default role: USER");
                });
    }
}
