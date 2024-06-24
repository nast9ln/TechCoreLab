package org.example.dao;

import org.example.entity.Role;
import org.example.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Интерфейс для работы с сущностью {@link Role} в базе данных.
 */
@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
