package org.example.dao;

import org.example.entity.Role;
import org.example.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Интерфейс для работы с сущностью Role в базе данных.
 * Предоставляет методы для поиска роли по идентификатору и по наименованию.
 */
@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);

//    /**
//     * Ищет роль по её идентификатору.
//     *
//     * @param id идентификатор роли, которую нужно найти.
//     * @return Optional объект, содержащий найденную роль или пустой, если роль не найдена.
//     */
//    Optional<Role> findById(Long id);
//
//    /**
//     * Ищет роль по её наименованию.
//     *
//     * @param name наименование роли, которую нужно найти.
//     * @return Optional объект, содержащий найденную роль или пустой, если роль не найдена.
//     */
//    Optional<Role> findByName(RoleEnum name);
}
