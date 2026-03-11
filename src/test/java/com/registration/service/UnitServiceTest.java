package com.registration.service;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final UserService service = new UserService(repository);

    @Test
    void shouldRegisterNewUserFromGoogle() {

        Map<String, Object> attributes = Map.of(
                "email", "john@gmail.com",
                "name", "John Doe"
        );

        when(repository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.empty());

        when(repository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        User user = service.registerOrUpdateUser(attributes);

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@gmail.com", user.getEmail());
    }
}
