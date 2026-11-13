
package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;

import java.util.List;

public class UserService {

    private final UserDao userDao = new UserDao();

    // CREATE
    public void createUser(String name, String email, int age) {
        User user = new User(name, email, age);
        userDao.save(user);
    }

    // READ
    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    // UPDATE
    public void updateUser(User user) {
        userDao.update(user);
    }

    // DELETE
    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}
