package org.example.service;

import org.example.dto.PersonDto;

/**
 * Сервис для работы с объектами {@link PersonDto}.
 */
public interface PersonService {
    /**
     * Создает новую запись о человеке на основе переданных данных.
     *
     * @param personDto объект с данными о человеке
     * @return объект PersonDto, представляющий созданную запись
     */
    PersonDto create(PersonDto personDto);

    /**
     * Возвращает данные о человеке по его идентификатору.
     *
     * @param id идентификатор человека
     * @return объект PersonDto с данными о человеке
     */
    PersonDto read(Long id);

    /**
     * Обновляет существующую запись о человеке на основе переданных данных.
     *
     * @param personDto объект с обновленными данными о человеке
     * @return объект PersonDto с обновленными данными
     */
    PersonDto update(PersonDto personDto);

    /**
     * Удаляет запись о человеке по его идентификатору.
     *
     * @param id идентификатор человека, чью запись необходимо удалить
     */
    void delete(Long id);

    void delete();
}
