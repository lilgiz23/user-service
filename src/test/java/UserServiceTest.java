package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_shouldReturnUser() {
        // Arrange
        User user = new User("Иван", "ivan@mail.com", 30);

        when(userDao.findById(1L)).thenReturn(user);

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertEquals(user, result);
        verify(userDao).findById(1L);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        // Arrange
        User user1 = new User("Иван", "ivan@mail.com", 30);
        User user2 = new User("Анна", "anna@mail.com", 25);

        List<User> users = List.of(user1, user2);

        when(userDao.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals(users, result);
        verify(userDao).findAll();
    }


    @Test
    void createUser_shouldSaveUser() {
        // Arrange
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        // Act
        userService.createUser("Иван", "ivan@mail.com", 30);

        // Assert
        verify(userDao).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Иван", savedUser.getName());
        assertEquals("ivan@mail.com", savedUser.getEmail());
        assertEquals(30, savedUser.getAge());
    }

    @Test
    void updateUser_shouldCallDaoUpdate() {
        // Arrange
        User user = new User("Иван", "ivan@mail.com", 30);

        // Act
        userService.updateUser(user);

        // Assert
        verify(userDao).update(user);
    }

    @Test
    void deleteUser_shouldCallDaoDelete() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userDao).delete(userId);
    }
}
