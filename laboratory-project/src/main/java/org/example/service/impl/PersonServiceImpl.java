package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.PersonDao;
import org.example.dao.RoleDao;
import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.example.entity.Role;
import org.example.enums.RoleEnum;
import org.example.exception.EntityNotFoundException;
import org.example.exception.LoginDuplicateException;
import org.example.mapper.PersonMapper;
import org.example.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final PersonMapper personMapper;
    private final RoleDao roleDao;

    @Override
    public PersonDto create(PersonDto personDto) {
        log.info("Creating person with data: {}", personDto);
        if (personDao.existsByLogin(personDto.getLogin())) {
            log.error("Duplicate login detected for {}", personDto.getLogin());
            throw new LoginDuplicateException("Login already in use: " + personDto.getLogin());
        }
        Person person = personMapper.toEntity(personDto);
        person.setRole(checkAndSetRole(person));
        PersonDto createdPersonDto = personMapper.toDto(personDao.save(person));
        log.info("Person created with ID: {}", createdPersonDto.getId());
        return createdPersonDto;
    }

    public PersonDto update(PersonDto personDto) {
        Person existingPerson = personDao.findById(personDto.getId())
                .orElseThrow(() -> {
                    log.error("Person with ID {} not found for update", personDto.getId());
                    return new EntityNotFoundException("Person with id {0} not found", personDto.getId());
                });

        Person newPerson = personMapper.update(existingPerson, personDto);

        Role role = roleDao.findById(newPerson.getRole().getId())
                .orElseThrow(() -> new EntityNotFoundException("Role with id {0} not found", newPerson.getRole().getId()));
        newPerson.setRole(role);

        personDao.save(newPerson);
        return personMapper.toDto(newPerson);
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    public PersonDto read(Long id) {
        log.info("Reading person with ID: {}", id);
        PersonDto personDto = personMapper.toDto(personDao.findById(id)
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

        Person existingPerson = personDao.findById(id)
                .orElseThrow(() -> {
                    log.error("Person with ID {} not found for update", id);
                    return new EntityNotFoundException("Person with id {0} not found", id);
                });

        personDao.delete(existingPerson);
        log.info("Person deleted with ID: {}", id);
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
                return roleDao.findById(roleId)
                        .orElseThrow(() -> {
                            log.error("Role with ID {} not found", roleId);
                            return new EntityNotFoundException("Cannot find role with ID: " + roleId);
                        });
            } else if (roleName != null) {
                log.info("Checking and setting role with name: {}", roleName);
                return roleDao.findByName(RoleEnum.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> {
                            log.error("Role with name {} not found", roleName);
                            return new EntityNotFoundException("Cannot find role with name: " + roleName);
                        });
            }
        }

        log.info("Role is null or not defined. Setting default role to USER.");
        return roleDao.findByName(RoleEnum.USER)
                .orElseThrow(() -> {
                    log.error("Default role USER not found");
                    return new EntityNotFoundException("Cannot find default role: USER");
                });
    }
}
