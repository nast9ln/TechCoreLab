package org.example.repository;

import org.example.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для работы с сущностью {@link Person} в базе данных.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByLogin(String login);

    Optional<Person> findByLogin(String username);
}
