package com.voluntrack.volunteerplatform.service.impl;

import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByUsername_shouldReturnUserWhenExists() {
        User user = new User();
        user.setUsername("admin");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("admin");

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("admin");
    }

    @Test
    void existsByUsername_shouldReturnTrueWhenUserExists() {
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        boolean result = userService.existsByUsername("admin");

        assertTrue(result);
        verify(userRepository, times(1)).existsByUsername("admin");
    }

    @Test
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        when(userRepository.existsByEmail("admin@voluntrack.com")).thenReturn(true);

        boolean result = userService.existsByEmail("admin@voluntrack.com");

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail("admin@voluntrack.com");
    }

    @Test
    void save_shouldReturnSavedUser() {
        User user = new User();
        user.setUsername("volunteer1");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals("volunteer1", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }
}