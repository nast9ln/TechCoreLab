package org.example.service.impl;

import lombok.RequiredArgsConstructor;
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
public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final PersonMapper personMapper;
    private final RoleDao roleDao;
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    public PersonDto create(PersonDto personDto) {
        logger.info("Creating person with data: {}", personDto);
        if (personDao.existsByLogin(personDto.getLogin())) {
            logger.error("Duplicate login detected for {}", personDto.getLogin());
            throw new LoginDuplicateException("Login already in use: " + personDto.getLogin());
        }
        Person person = personMapper.toEntity(personDto);
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

        personDao.save(newPerson);
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

        Person existingPerson = personDao.findById(id)
                .orElseThrow(() -> {
                    logger.error("Person with ID {} not found for update", id);
                    return new EntityNotFoundException("Person with id {0} not found", id);
                });

        personDao.delete(existingPerson);
        logger.info("Person deleted with ID: {}", id);
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
                logger.info("Checking and setting role with ID: {}", roleId);
                return roleDao.findById(roleId)
                        .orElseThrow(() -> {
                            logger.error("Role with ID {} not found", roleId);
                            return new EntityNotFoundException("Cannot find role with ID: " + roleId);
                        });
            } else if (roleName != null) {
                logger.info("Checking and setting role with name: {}", roleName);
                return roleDao.findByName(RoleEnum.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> {
                            logger.error("Role with name {} not found", roleName);
                            return new EntityNotFoundException("Cannot find role with name: " + roleName);
                        });
            }
        }

        logger.info("Role is null or not defined. Setting default role to USER.");
        return roleDao.findByName(RoleEnum.USER)
                .orElseThrow(() -> {
                    logger.error("Default role USER not found");
                    return new EntityNotFoundException("Cannot find default role: USER");
                });
    }
}
