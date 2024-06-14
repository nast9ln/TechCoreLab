package org.example.dao;

import org.example.entity.Person;

/**
 * Интерфейс для работы с сущностью Person в базе данных.
 * Наследует базовые методы CRUD из BaseDao.
 * Предоставляет дополнительный метод для проверки существования Person по логину.
 */
public interface PersonDao extends BaseDao<Long, Person> {

    /**
     * Проверяет, существует ли в базе данных Person с указанным логином.
     *
     * @param login логин для проверки.
     * @return true, если Person с указанным логином существует, иначе false.
     */
    boolean existsByLogin(String login);
}
