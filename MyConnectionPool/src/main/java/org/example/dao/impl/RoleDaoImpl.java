//package org.example.dao.impl;
//
//import org.example.dao.RoleDao;
//import org.example.entity.Role;
//import org.example.enums.RoleEnum;
//import org.example.util.HibernateSessionFactory;
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Optional;
//
//public class RoleDaoImpl implements RoleDao {
//    private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
//
//    @Override
//    public Optional<Role> findById(Long id) {
//        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
//            Role role = session.get(Role.class, id);
//            return Optional.ofNullable(role);
//        } catch (Exception e) {
//            logger.error("Failed to find role by ID: {}", id, e);
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Optional<Role> findByName(RoleEnum name) {
//        String hql = "FROM Role R WHERE R.name = :roleName";
//        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
//            Query<Role> query = session.createQuery(hql, Role.class);
//            query.setParameter("roleName", name);
//            Role role = query.uniqueResult();
//            return Optional.ofNullable(role);
//        } catch (Exception e) {
//            logger.error("Failed to find role by name: {}", name, e);
//            return Optional.empty();
//        }
//    }
//}
