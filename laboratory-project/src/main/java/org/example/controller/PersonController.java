package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonDto;
import org.example.entity.Person;
import org.example.service.impl.PersonServiceImpl;
import org.springframework.http.ResponseEntity;
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
     * Возвращает объект Person по заданному идентификатору.
     *
     * @param id идентификатор объекта Person
     * @return объект PersonDto с заданным идентификатором
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> read(@PathVariable Long id) {
        log.info("read");
        return ResponseEntity.ok(personService.read(id));
    }

    /**
     * Обновляет существующий объект Person.
     *
     * @param dto объект PersonDto, содержащий обновленные данные для объекта Person
     * @return ответ без тела с HTTP-статусом
     */
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody PersonDto dto) {
        log.info("update");
        personService.update(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет объект Person по заданному идентификатору.
     *
     * @param id идентификатор объекта Person, который необходимо удалить
     * @return ответ без тела с HTTP-статусом
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("delete");
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        log.info("delete");
        personService.delete();
        return ResponseEntity.ok().build();
    }

}
