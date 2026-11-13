
package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.example.entity.User;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Создание SessionFactory
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

    // Получение SessionFactory
    public static synchronized SessionFactory getSessionFactory() {

        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = buildSessionFactory();
        }

        return sessionFactory;
    }

    // Закрытие SessionFactory
    public static void shutdown() {

        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
