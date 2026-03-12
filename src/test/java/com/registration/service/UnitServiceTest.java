package com.registration.service;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService service;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        service = new UserService(userRepository);
    }

    @Test
    void testRegisterOrUpdateUser_NewUser() {
        // Create a User object (not a Map)
        User newUser = new User();
        newUser.setEmail("ken@example.com");
        newUser.setFirstName("Ken");
        newUser.setLastName("Milota");

        // Mock repository behavior
        when(userRepository.findByEmail("ken@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Call the method
        User savedUser = service.registerOrUpdateUser(newUser);

        // Verify repository methods called
        verify(userRepository).findByEmail("ken@example.com");
        verify(userRepository).save(newUser);

        // Assertions
        assertEquals("Ken", savedUser.getFirstName());
        assertEquals("Milota", savedUser.getLastName());
        assertEquals("ken@example.com", savedUser.getEmail());
    }

    @Test
    void testRegisterOrUpdateUser_ExistingUser() {
        // Existing user in DB
        User existingUser = new User();
        existingUser.setEmail("ken@example.com");
        existingUser.setFirstName("OldFirst");
        existingUser.setLastName("OldLast");

        // Attributes from Google (simulated)
        User googleUser = new User();
        googleUser.setEmail("ken@example.com");
        googleUser.setFirstName("Ken");
        googleUser.setLastName("Milota");

        when(userRepository.findByEmail("ken@example.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updatedUser = service.registerOrUpdateUser(googleUser);

        assertEquals("Ken", updatedUser.getFirstName());
        assertEquals("Milota", updatedUser.getLastName());
        assertEquals("ken@example.com", updatedUser.getEmail());
    }
}