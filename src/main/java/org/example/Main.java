package org.example;

import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.HibernateUtil;
import java.util.List;
import java.util.Scanner;
import org.example.dao.UserDao;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserService(new UserDao());
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        try {

            while (running) {

                System.out.println("\n===== USER SERVICE =====");
                System.out.println("1. Добавить пользователя");
                System.out.println("2. Найти пользователя по ID");
                System.out.println("3. Показать всех пользователей");
                System.out.println("4. Изменить пользователя");
                System.out.println("5. Удалить пользователя");
                System.out.println("0. Выход");

                int choice = readMenuChoice(scanner);

                try {

                    switch (choice) {

                        // CREATE
                        case 1: {
                            String name = readRequiredText(scanner, "Введите имя: ");
                            String email = readRequiredText(scanner, "Введите email: ");

                            int age = readAge(scanner);

                            userService.createUser(name, email, age);

                            System.out.println("Пользователь добавлен!");
                            break;
                        }

                        // READ BY ID
                        case 2: {
                            long id = readId(scanner);

                            User user = userService.getUserById(id);

                            if (user != null) {
                                printUser(user);
                            } else {
                                System.out.println("Пользователь не найден!");
                            }

                            break;
                        }

                        // READ ALL
                        case 3: {
                            List<User> users = userService.getAllUsers();

                            if (users.isEmpty()) {
                                System.out.println("Пользователей пока нет.");
                            } else {
                                for (User currentUser : users) {
                                    System.out.println("--------------------");
                                    printUser(currentUser);
                                }

                                System.out.println("--------------------");
                            }

                            break;
                        }

                        // UPDATE
                        case 4: {
                            long updateId = readId(scanner);

                            User userToUpdate = userService.getUserById(updateId);

                            if (userToUpdate != null) {

                                String newName = readRequiredText(scanner, "Введите новое имя: ");
                                String newEmail = readRequiredText(scanner, "Введите новый email: ");

                                int newAge = readAge(scanner);

                                userToUpdate.setName(newName);
                                userToUpdate.setEmail(newEmail);
                                userToUpdate.setAge(newAge);

                                userService.updateUser(userToUpdate);

                                System.out.println("Пользователь обновлён!");

                            } else {
                                System.out.println("Пользователь не найден!");
                            }

                            break;
                        }

                        // DELETE
                        case 5: {
                            long deleteId = readId(scanner);

                            User userToDelete = userService.getUserById(deleteId);

                            if (userToDelete != null) {
                                userService.deleteUser(deleteId);
                                System.out.println("Пользователь удалён!");
                            } else {
                                System.out.println("Пользователь не найден!");
                            }

                            break;
                        }

                        // EXIT
                        case 0: {
                            running = false;
                            System.out.println("Программа завершена.");
                            break;
                        }

                        default:
                            System.out.println("Неверный выбор!");
                    }

                } catch (RuntimeException exception) {

                    System.out.println("Не удалось выполнить операцию.");
                    System.out.println("Причина: " + exception.getMessage());
                    System.out.println("Подробности ошибки записаны в лог.");
                }
            }

        } finally {

            scanner.close();
            HibernateUtil.shutdown();

        }
    }

    // Проверка выбора пункта меню
    private static int readMenuChoice(Scanner scanner) {

        while (true) {

            System.out.print("Выберите действие: ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);

                if (choice >= 0 && choice <= 5) {
                    return choice;
                }

                System.out.println("Ошибка! Выберите пункт от 0 до 5.");

            } catch (NumberFormatException exception) {
                System.out.println("Ошибка! Введите номер пункта числом.");
            }
        }
    }

    // Проверка ID
    private static long readId(Scanner scanner) {

        while (true) {

            System.out.print("Введите ID пользователя: ");
            String input = scanner.nextLine();

            try {
                long id = Long.parseLong(input);

                if (id > 0) {
                    return id;
                }

                System.out.println("Ошибка! ID должен быть больше нуля.");

            } catch (NumberFormatException exception) {
                System.out.println("Ошибка! Введите ID числом.");
            }
        }
    }

    // Проверка возраста
    private static int readAge(Scanner scanner) {

        while (true) {

            System.out.print("Введите возраст: ");
            String input = scanner.nextLine();

            try {
                int age = Integer.parseInt(input);

                if (age >= 0 && age <= 120) {
                    return age;
                }

                System.out.println("Ошибка! Возраст должен быть от 0 до 120.");

            } catch (NumberFormatException exception) {
                System.out.println("Ошибка! Введите возраст числом.");
            }
        }
    }

    // Проверка имени и email
    private static String readRequiredText(Scanner scanner, String prompt) {

        while (true) {

            System.out.print(prompt);

            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Ошибка! Поле не должно быть пустым.");
        }
    }

    // Вывод информации о пользователе
    private static void printUser(User user) {

        System.out.println("ID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Дата создания: " + user.getCreatedAt());
    }
}
