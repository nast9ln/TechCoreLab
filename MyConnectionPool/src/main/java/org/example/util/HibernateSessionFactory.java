package org.example.util;

import org.example.entity.Person;
import org.example.entity.Role;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(HibernateSessionFactory.class);

    private HibernateSessionFactory() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                logger.info("Configuring Hibernate session factory");
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Person.class);
                configuration.addAnnotatedClass(Role.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
                logger.info("Hibernate session factory created successfully");
            } catch (Exception e) {
                logger.error("Exception while building Hibernate session factory: {}", e.getMessage(), e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }
}
