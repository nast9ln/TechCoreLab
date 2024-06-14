package org.example.dao.impl;

import org.example.dao.PersonDao;
import org.example.dao.RoleDao;
import org.example.entity.Person;
import org.example.exception.EntityNotFoundException;
import org.example.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PersonDaoImpl implements PersonDao {
    private static final Logger logger = LoggerFactory.getLogger(PersonDaoImpl.class);
    private final RoleDao roleDao;

    public PersonDaoImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Person save(Person person) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(person);
            transaction.commit();
            logger.info("Person saved successfully: {}", person);
            return person;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                logger.error("Transaction rolled back due to an error");
            }
            logger.error("Error saving person: {}", person, e);
            throw new RuntimeException("Failed to save person", e);
        }
    }


    @Override
    public Person update(Person person) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(person);
            transaction.commit();
            logger.info("Person updated successfully: {}", person);
            return person;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error updating person: {}", person, e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Person person = session.get(Person.class, id);
            if (person != null) {
                session.remove(person);
                logger.info("Person deleted successfully: {}", person);
            } else {
                throw new EntityNotFoundException("Person not found");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error deleting person with ID: {}", id, e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Optional<Person> findById(Long id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Person person = session.get(Person.class, id);
            if (person != null) {
                person.setRole(roleDao.findById(person.getRole().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Role not found")));
                logger.info("Person found with ID: {}", id);
                return Optional.of(person);
            } else {
                logger.warn("Person not found with ID: {}", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error finding person with ID: {}", id, e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        String hql = "SELECT count(p.id) FROM Person p WHERE p.login = :login";
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("login", login);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error("Failed to check for existing login due to an error: {}", e.getMessage());
            throw new RuntimeException("Database error checking login existence", e);
        }
    }

}
