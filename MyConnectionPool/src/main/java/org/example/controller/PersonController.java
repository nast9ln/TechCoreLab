package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.example.service.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для управления сущностями {@link Person}.
 * Обрабатывает HTTP-запросы для создания, чтения, обновления и удаления объектов {@link Person}.
 */
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonServiceImpl personService;
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    /**
     * Создает новый объект Person.
     *
     * @param dto объект PersonDto, содержащий данные для создания нового объекта Person
     * @return созданный объект PersonDto
     */
    @PostMapping
    public PersonDto create(@RequestBody PersonDto dto) {
        logger.info("create");
        return personService.create(dto);
    }

    /**
     * Возвращает объект Person по заданному идентификатору.
     *
     * @param id идентификатор объекта Person
     * @return объект PersonDto с заданным идентификатором
     */
    @GetMapping("/{id}")
    public PersonDto read(@PathVariable Long id) {
        logger.info("read");
        return personService.read(id);
    }

    /**
     * Обновляет существующий объект Person.
     *
     * @param dto объект PersonDto, содержащий обновленные данные для объекта Person
     */
    @PutMapping
    public void update(@RequestBody PersonDto dto) {
        logger.info("update");
        personService.update(dto);
    }

    /**
     * Удаляет объект Person по заданному идентификатору.
     *
     * @param id идентификатор объекта Person, который необходимо удалить
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("delete");
        personService.delete(id);
    }
}
