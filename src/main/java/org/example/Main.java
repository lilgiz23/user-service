package org.example;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final UserDao USER_DAO = new UserDao();

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Выберите действие: ");
            switch (choice) {
                case 1 -> addUser();
                case 2 -> showAll();
                case 3 -> findUser();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> running = false;
                default -> System.out.println("Неверный ввод.");
            }
        }

        System.out.println("Выход...");
        HibernateUtil.shutdown();
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("\n--- МЕНЮ ---");
        System.out.println("1. Добавить пользователя");
        System.out.println("2. Показать всех пользователей");
        System.out.println("3. Найти по ID");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
    }

    private static void addUser() {
        String name = readNonEmpty("Введите имя: ");
        String email = readEmail("Введите email: ");
        int age = readAge("Введите возраст: ");
        USER_DAO.save(new User(name, email, age));
    }

    private static void showAll() {
        List<User> users = USER_DAO.findAll();
        if (users.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void findUser() {
        Long id = readLong("Введите ID: ");
        User user = USER_DAO.findById(id);
        System.out.println(user != null ? user : "Пользователь не найден.");
    }

    private static void updateUser() {
        Long id = readLong("Введите ID для обновления: ");
        User user = USER_DAO.findById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        String name = readLine("Новое имя (Enter, чтобы оставить): ");
        if (!name.isEmpty()) {
            user.setName(name);
        }

        String email = readLine("Новый email (Enter, чтобы оставить): ");
        if (!email.isEmpty()) {
            if (!isValidEmail(email)) {
                System.out.println("Некорректный email, обновление отменено.");
                return;
            }
            user.setEmail(email);
        }

        String ageText = readLine("Новый возраст (Enter, чтобы оставить): ");
        if (!ageText.isEmpty()) {
            try {
                int age = Integer.parseInt(ageText);
                if (age < 0 || age > 150) {
                    System.out.println("Некорректный возраст, обновление отменено.");
                    return;
                }
                user.setAge(age);
            } catch (NumberFormatException e) {
                System.out.println("Возраст должен быть числом, обновление отменено.");
                return;
            }
        }

        USER_DAO.update(user);
    }

    private static void deleteUser() {
        Long id = readLong("Введите ID для удаления: ");
        USER_DAO.delete(id);
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Поле не может быть пустым.");
        }
    }

    private static String readEmail(String prompt) {
        while (true) {
            String email = readLine(prompt);
            if (isValidEmail(email)) {
                return email;
            }
            System.out.println("Некорректный email (нужен символ @).");
        }
    }

    private static boolean isValidEmail(String email) {
        return email.contains("@") && !email.startsWith("@") && !email.endsWith("@");
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести целое число.");
            }
        }
    }

    private static int readAge(String prompt) {
        while (true) {
            int age = readInt(prompt);
            if (age >= 0 && age <= 150) {
                return age;
            }
            System.out.println("Возраст должен быть от 0 до 150.");
        }
    }

    private static Long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести целое число.");
            }
        }
    }
}