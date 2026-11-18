
package org.example.util;

import org.example.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Создание основной SessionFactory
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(User.class);

            return configuration.buildSessionFactory();

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Не удалось создать SessionFactory. Проверьте подключение к базе данных.",
                    exception
            );
        }
    }

    // Получение основной SessionFactory
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = buildSessionFactory();
        }

        return sessionFactory;
    }

    // Создание отдельной SessionFactory для тестовой базы
    public static SessionFactory createTestSessionFactory(
            String jdbcUrl,
            String username,
            String password
    ) {
        Configuration configuration = new Configuration();

        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);

        // Подключение к PostgreSQL из Testcontainers
        configuration.setProperty("hibernate.connection.url", jdbcUrl);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);

        // Создание и удаление таблиц тестовой базы
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        return configuration.buildSessionFactory();
    }

    // Закрытие основной SessionFactory
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
