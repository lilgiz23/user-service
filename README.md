
# User Service

Консольное Java-приложение для управления пользователями с использованием Hibernate и PostgreSQL.

Проект разработан в рамках учебного задания по работе с ORM Hibernate и базами данных.

## Технологии

- Java 25
- Maven
- Hibernate ORM 7.2.0
- PostgreSQL
- SLF4J
- Logback

## Возможности

Приложение поддерживает основные CRUD-операции:

- CREATE — добавление пользователя
- READ — поиск пользователя по ID
- READ ALL — получение списка пользователей
- UPDATE — изменение данных пользователя
- DELETE — удаление пользователя

Сущность User содержит поля:

- id — идентификатор
- name — имя
- email — электронная почта
- age — возраст
- created_at — дата и время создания

## Требования

Для запуска необходимы:

- JDK 25
- Maven
- PostgreSQL

## Настройка базы данных

Создайте базу данных:

```sql
CREATE DATABASE user_service;
```

Откройте файл:

`src/main/resources/hibernate.cfg.xml`

При необходимости измените настройки подключения:

- URL базы данных
- имя пользователя PostgreSQL
- пароль PostgreSQL

## Запуск приложения

1. Откройте проект в IntelliJ IDEA.
2. Дождитесь загрузки Maven-зависимостей.
3. Убедитесь, что PostgreSQL запущен.
4. Откройте класс `Main.java`.
5. Запустите метод `main()`.

Hibernate автоматически создаёт и обновляет таблицу `users`.

## Структура проекта

```text
src/main/java/org/example/
├── Main.java
├── dao/
│   └── UserDao.java
├── entity/
│   └── User.java
├── service/
│   └── UserService.java
└── util/
    └── HibernateUtil.java

src/main/resources/
├── hibernate.cfg.xml
└── logback.xml
```

## Архитектура

- Main — консольное взаимодействие с пользователем.
- UserService — бизнес-логика приложения.
- UserDao — работа с базой данных.
- User — сущность пользователя.
- HibernateUtil — настройка и получение SessionFactory.

Для выполнения операций записи используются транзакции. Ошибки обрабатываются, основные события записываются в лог.

## Автор

Гаймалетдинов Ильгиз
