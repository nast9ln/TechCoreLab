package org.example.service.impl;

import jakarta.transaction.Transactional;
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

@Transactional
public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final PersonMapper personMapper;
    private final RoleDao roleDao;
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    public PersonServiceImpl(PersonDao personDao, PersonMapper personMapper, RoleDao roleDao) {
        this.personDao = personDao;
        this.personMapper = personMapper;
        this.roleDao = roleDao;
    }

    @Override
    public PersonDto create(PersonDto personDto) {
        logger.info("Creating person with data: {}", personDto);
        if (personDao.existsByLogin(personDto.getLogin())) {
            logger.error("Duplicate login detected for {}", personDto.getLogin());
            throw new LoginDuplicateException("Login already in use: " + personDto.getLogin());
        }
        Person person = personMapper.create(personDto);
        person.setRole(checkAndSetRole(person));
        PersonDto createdPersonDto = personMapper.toDto(personDao.save(person));
        logger.info("Person created with ID: {}", createdPersonDto.getId());
        return createdPersonDto;
    }

    public PersonDto update(PersonDto personDto) {
        Person existingPerson = personDao.findById(personDto.getId())
                .orElseThrow(() -> {
                    logger.error("Person with ID {} not found for update", personDto.getId());
                    return new EntityNotFoundException("Person with id {0} not found", personDto.getId());
                });

        Person newPerson = personMapper.update(existingPerson, personDto);

        Role role = roleDao.findById(newPerson.getRole().getId())
                .orElseThrow(() -> new EntityNotFoundException("Role with id {0} not found", newPerson.getRole().getId()));
        newPerson.setRole(role);

        personDao.update(newPerson);
        return personMapper.toDto(newPerson);
    }


    @Override
    public PersonDto read(Long id) {
        logger.info("Reading person with ID: {}", id);
        PersonDto personDto = personMapper.toDto(personDao.findById(id)
                .orElseThrow(() -> {
                    logger.error("Person with ID {} not found", id);
                    return new EntityNotFoundException("Person with id " + id + " not found");
                }));
        logger.info("Person read with ID: {}", personDto.getId());
        return personDto;
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting person with ID: {}", id);
        personDao.delete(id);
        logger.info("Person deleted with ID: {}", id);
    }

    private Role checkAndSetRole(Person person) {
        Role role = person.getRole();
        if (role == null || role.getName() == null) {
            logger.info("Role is null or not defined. Setting default role to USER.");
            return roleDao.findByName(RoleEnum.USER)
                    .orElseThrow(() -> {
                        logger.error("Default role USER not found");
                        return new EntityNotFoundException("Cannot find role: USER");
                    });
        } else {
            logger.info("Checking and setting role with name: {}", role.getName());
            return roleDao.findByName(role.getName())
                    .orElseThrow(() -> {
                        logger.error("Cannot find role with name: {}", role.getName());
                        return new EntityNotFoundException("Cannot find role: " + role.getName());
                    });
        }
    }
}
