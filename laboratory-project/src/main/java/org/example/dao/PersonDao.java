package org.example.dao;

import org.example.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для работы с сущностью {@link Person} в базе данных.
 */
@Repository
public interface PersonDao extends JpaRepository<Person, Long> {
    boolean existsByLogin(String login);
}
