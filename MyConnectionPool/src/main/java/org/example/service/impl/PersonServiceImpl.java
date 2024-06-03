package org.example.service.impl;

import org.example.dao.PersonDao;
import org.example.dao.RoleDao;
import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.example.entity.Role;
import org.example.enums.RoleEnum;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.PersonMapper;
import org.example.service.PersonService;

import java.util.Objects;

public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final PersonMapper personMapper;
    private final RoleDao roleDao;

    public PersonServiceImpl(PersonDao personDao, PersonMapper personMapper, RoleDao roleDao) {
        this.personDao = personDao;
        this.personMapper = personMapper;
        this.roleDao = roleDao;
    }


    public PersonDto create(PersonDto personDto) {
        Person person = personMapper.toEntity(personDto);
        person.setId(null);
        if (Objects.isNull(person.getRole())) {
            person.setRole(roleDao.findByName(RoleEnum.USER).orElseThrow(()->new EntityNotFoundException("Cannot find role")));
        } else if (Objects.nonNull(person.getRole().getName())){
            person.setRole(roleDao.findByName(person.getRole().getName()).orElseThrow(()-> new EntityNotFoundException("Cannot find role")));
        } else if (Objects.nonNull(person.getRole().getId())){
            person
                    .setRole(roleDao.findById(personDto.getRole().getId()).orElseThrow(()-> new EntityNotFoundException("Cannot find role")));
        }
        return personMapper.toDto(personDao.save(person));
    }

    public PersonDto update(PersonDto personDto) {
        personDao.findById(personDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Person with id {0} not found", personDto.getId()));
        Person person = personMapper.toEntity(personDto);
        return personMapper.toDto(personDao.update(person));

    }

    public PersonDto read(Long id) {
        return personMapper.toDto(personDao.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Person with id {0} not found", id)));
    }

    public void delete(Long id) {
        personDao.delete(id);
    }
}
