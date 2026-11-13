
package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDao {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDao.class);

    // CREATE — сохранение пользователя
    public void save(User user) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

            logger.info("Пользователь успешно добавлен. ID: {}", user.getId());

        } catch (Exception exception) {

            rollback(transaction);

            logger.error("Ошибка при добавлении пользователя", exception);

            throw new RuntimeException("Не удалось добавить пользователя", exception);
        }
    }

    // READ — поиск пользователя по ID
    public User findById(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            User user = session.find(User.class, id);

            if (user != null) {
                logger.info("Найден пользователь. ID: {}", id);
            } else {
                logger.warn("Пользователь не найден. ID: {}", id);
            }

            return user;

        } catch (Exception exception) {

            logger.error("Ошибка при поиске пользователя. ID: {}", id, exception);

            throw new RuntimeException("Не удалось найти пользователя", exception);
        }
    }

    // READ — получение всех пользователей
    public List<User> findAll() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<User> users = session
                    .createQuery("FROM User", User.class)
                    .getResultList();

            logger.info("Получен список пользователей. Количество: {}", users.size());

            return users;

        } catch (Exception exception) {

            logger.error("Ошибка при получении списка пользователей", exception);

            throw new RuntimeException("Не удалось получить список пользователей", exception);
        }
    }

    // UPDATE — обновление пользователя
    public void update(User user) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();

            logger.info("Пользователь обновлён. ID: {}", user.getId());

        } catch (Exception exception) {

            rollback(transaction);

            logger.error("Ошибка при обновлении пользователя. ID: {}", user.getId(), exception);

            throw new RuntimeException("Не удалось обновить пользователя", exception);
        }
    }

    // DELETE — удаление пользователя
    public void delete(Long id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            User user = session.find(User.class, id);

            if (user != null) {

                session.remove(user);

                transaction.commit();

                logger.info("Пользователь удалён. ID: {}", id);

            } else {

                transaction.rollback();

                logger.warn("Пользователь для удаления не найден. ID: {}", id);
            }

        } catch (Exception exception) {

            rollback(transaction);

            logger.error("Ошибка при удалении пользователя. ID: {}", id, exception);

            throw new RuntimeException("Не удалось удалить пользователя", exception);
        }
    }

    // Откат транзакции
    private void rollback(Transaction transaction) {

        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
            logger.warn("Транзакция отменена");
        }
    }
}
