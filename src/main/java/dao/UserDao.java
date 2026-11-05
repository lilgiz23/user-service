package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    public void save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
            } catch (RuntimeException e) {
                rollback(transaction);
                throw e;
            }
            LOGGER.info("Пользователь сохранён, id={}", user.getId());
            System.out.println("Пользователь добавлен! ID: " + user.getId());
        } catch (ConstraintViolationException e) {
            LOGGER.warn("Нарушено ограничение: {}", e.getMessage());
            System.out.println("Ошибка: пользователь с таким email уже существует.");
        } catch (HibernateException e) {
            LOGGER.error("Ошибка Hibernate при сохранении", e);
            System.out.println("Ошибка базы данных. Подробности в логе.");
        }
    }

    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            LOGGER.error("Ошибка Hibernate при поиске id={}", id, e);
            return null;
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (HibernateException e) {
            LOGGER.error("Ошибка Hibernate при чтении списка", e);
            return Collections.emptyList();
        }
    }

    public void update(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(user);
                transaction.commit();
            } catch (RuntimeException e) {
                rollback(transaction);
                throw e;
            }
            LOGGER.info("Пользователь обновлён, id={}", user.getId());
            System.out.println("Пользователь обновлён!");
        } catch (ConstraintViolationException e) {
            LOGGER.warn("Нарушено ограничение: {}", e.getMessage());
            System.out.println("Ошибка: такой email уже занят.");
        } catch (HibernateException e) {
            LOGGER.error("Ошибка Hibernate при обновлении", e);
            System.out.println("Ошибка базы данных. Подробности в логе.");
        }
    }

    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                if (user == null) {
                    rollback(transaction);
                    System.out.println("Пользователь с ID " + id + " не найден.");
                    return;
                }
                session.remove(user);
                transaction.commit();
            } catch (RuntimeException e) {
                rollback(transaction);
                throw e;
            }
            LOGGER.info("Пользователь удалён, id={}", id);
            System.out.println("Пользователь удалён!");
        } catch (HibernateException e) {
            LOGGER.error("Ошибка Hibernate при удалении", e);
            System.out.println("Ошибка базы данных. Подробности в логе.");
        }
    }

    private void rollback(Transaction transaction) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}