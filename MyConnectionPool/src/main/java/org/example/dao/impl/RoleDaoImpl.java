package org.example.dao.impl;

import org.example.dao.RoleDao;
import org.example.entity.Role;
import org.example.enums.RoleEnum;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RoleDaoImpl implements RoleDao {
    private static final String FIND_BY_ID = "SELECT id, name FROM techcore.role WHERE id = ? ";
    private static final String FIND_BY_NAME = "SELECT id, name FROM techcore.role WHERE name = ? ";


    @Override
    public Optional<Role> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Role role = Role.builder()
                        .id(resultSet.getLong("id"))
                        .name(RoleEnum.valueOf(resultSet.getString("name")))
                        .build();
                return Optional.ofNullable(role);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public Optional<Role> findByName(RoleEnum name) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, name.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Role role = Role.builder()
                        .id(resultSet.getLong("id"))
                        .name(RoleEnum.valueOf(resultSet.getString("name")))
                        .build();
                return Optional.ofNullable(role);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error");
        }
    }
}
