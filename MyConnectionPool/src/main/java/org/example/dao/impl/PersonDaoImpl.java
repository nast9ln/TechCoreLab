package org.example.dao.impl;

import org.example.dao.PersonDao;
import org.example.dao.RoleDao;
import org.example.entity.Person;
import org.example.exception.EntityNotFoundException;
import org.example.exception.LoginDuplicateException;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.time.ZoneId;
import java.util.Optional;

public class PersonDaoImpl implements PersonDao {
    private static final String INSERT_PERSON = "insert into techcore.person (name, registration_date, login, role) values (?, ?, ?, ?)";
    private static final String UPDATE_PERSON = "update techcore.person set name= ?, registration_date = ?, login = ?, role = ? WHERE id = ?";
    private static final String DELETE_PERSON = "delete from techcore.person where id = ?";
    private static final String FIND_BY_ID = "SELECT id, name, registration_date, login, role FROM techcore.person WHERE id = ? ";
    private final RoleDao roleDao;

    public PersonDaoImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Person save(Person person) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_PERSON, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, person.getName());
            statement.setDate(2, Date.valueOf(person.getRegistrationDate().atZone(ZoneId.systemDefault()).toLocalDate()));
            statement.setString(3, person.getLogin());
            statement.setLong(4, person.getRole().getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId(generatedKeys.getLong(1));
                return person;
            } else {
                throw new LoginDuplicateException("Creating person failed, no ID obtained");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new LoginDuplicateException("the username is already in use ");
        }
    }


    @Override
    public Person update(Person person) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PERSON)) {
            statement.setString(1, person.getName());
            statement.setTimestamp(2, Timestamp.from(person.getRegistrationDate()));
            statement.setString(3, person.getLogin());
            statement.setLong(4, person.getRole().getId());
            statement.setLong(5, person.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return person;
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(DELETE_PERSON)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Person> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Person person = Person.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .login(resultSet.getString("login"))
                        .registrationDate(resultSet.getTimestamp("registration_date").toInstant())
                        .role(roleDao.findById(resultSet.getLong("role")).orElseThrow(() -> new EntityNotFoundException("Role not found")))
                        .build();
                return Optional.of(person);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
