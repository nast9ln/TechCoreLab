package org.example.dao;

import org.example.entity.Role;
import org.example.enums.RoleEnum;

import java.util.Optional;

public interface RoleDao {
    Optional<Role> findById(Long id);

    Optional<Role> findByName(RoleEnum name);
}
