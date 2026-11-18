
package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("test_db")
                    .withUsername("test")
                    .withPassword("test");

    private static SessionFactory sessionFactory;
    private static UserDao userDao;

    // Выполняется один раз перед всеми тестами
    @BeforeAll
    static void setUp() {

        sessionFactory = HibernateUtil.createTestSessionFactory(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        userDao = new UserDao(sessionFactory);
    }

    // Очистка таблицы перед каждым тестом
    @BeforeEach
    void cleanDatabase() {

        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            session.createMutationQuery("DELETE FROM User")
                    .executeUpdate();

            transaction.commit();
        }
    }

    // Закрываем SessionFactory после всех тестов
    @AfterAll
    static void tearDown() {

        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    // CREATE + READ
    @Test
    void save_shouldSaveUser() {

        User user = new User("Иван", "ivan@mail.com", 25);

        userDao.save(user);

        assertNotNull(user.getId());

        User foundUser = userDao.findById(user.getId());

        assertNotNull(foundUser);
        assertEquals("Иван", foundUser.getName());
        assertEquals("ivan@mail.com", foundUser.getEmail());
        assertEquals(25, foundUser.getAge());
    }

    // READ
    @Test
    void findAll_shouldReturnUsers() {

        userDao.save(new User("Анна", "anna@mail.com", 22));
        userDao.save(new User("Олег", "oleg@mail.com", 30));

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    // UPDATE
    @Test
    void update_shouldUpdateUser() {

        User user = new User("Пётр", "petr@mail.com", 28);
        userDao.save(user);

        user.setName("Пётр Иванов");
        user.setAge(29);

        userDao.update(user);

        User updatedUser = userDao.findById(user.getId());

        assertNotNull(updatedUser);
        assertEquals("Пётр Иванов", updatedUser.getName());
        assertEquals(29, updatedUser.getAge());
    }

    // DELETE
    @Test
    void delete_shouldDeleteUser() {

        User user = new User("Мария", "maria@mail.com", 27);
        userDao.save(user);

        Long userId = user.getId();

        userDao.delete(userId);

        User deletedUser = userDao.findById(userId);

        assertNull(deletedUser);
    }
}
