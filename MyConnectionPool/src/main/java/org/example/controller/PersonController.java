package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PersonDto;
import org.example.service.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonServiceImpl personService;
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @PostMapping
    public PersonDto create(@RequestBody PersonDto dto) {
        logger.info("create");
        return personService.create(dto);
    }

    @GetMapping("/{id}")
    public PersonDto read(@PathVariable Long id) {
        logger.info("read");
        return personService.read(id);
    }

    @PutMapping
    public void update(@RequestBody PersonDto dto) {
        logger.info("update");
        personService.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("delete");
        personService.delete(id);
    }
}