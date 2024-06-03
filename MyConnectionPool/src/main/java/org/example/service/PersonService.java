package org.example.service;

import org.example.dto.PersonDto;

public interface PersonService {
    PersonDto create(PersonDto personDto);

    PersonDto read(Long id);

    PersonDto update(PersonDto personDto);

    void delete(Long id);

}
