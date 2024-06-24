package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.example.service.impl.PersonServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для управления сущностями {@link Person}.
 * Обрабатывает HTTP-запросы для создания, чтения, обновления и удаления объектов {@link Person}.
 */
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {

    private final PersonServiceImpl personService;

    /**
     * Создает новый объект Person.
     *
     * @param dto объект PersonDto, содержащий данные для создания нового объекта Person
     * @return созданный объект PersonDto
     */
    @PostMapping
    public PersonDto create(@RequestBody PersonDto dto) {
        log.info("create");
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
        log.info("read");
        return personService.read(id);
    }

    /**
     * Обновляет существующий объект Person.
     *
     * @param dto объект PersonDto, содержащий обновленные данные для объекта Person
     */
    @PutMapping
    public void update(@RequestBody PersonDto dto) {
        log.info("update");
        personService.update(dto);
    }

    /**
     * Удаляет объект Person по заданному идентификатору.
     *
     * @param id идентификатор объекта Person, который необходимо удалить
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("delete");
        personService.delete(id);
    }
}
