package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    private final UserService userService = new UserService();

    @Test
    public void testSaveUser_whenUserDoesNotExist() {
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password");

        // Настройка мок-объекта
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        userService.userRepository = userRepository;
        userService.bCryptPasswordEncoder = bCryptPasswordEncoder;

        boolean saved = userService.saveUser(user);

        assertTrue(saved);
        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    public void testSaveUser_whenUserExists() {
        User existingUser = new User();
        existingUser.setUsername("john_doe");

        User newUser = new User();
        newUser.setUsername("john_doe");

        // Настройка мок-объекта
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(existingUser);

        userService.userRepository = userRepository;
        userService.bCryptPasswordEncoder = bCryptPasswordEncoder;

        boolean saved = userService.saveUser(newUser);

        assertFalse(saved);
        verify(userRepository, never()).save(newUser);
    }
}