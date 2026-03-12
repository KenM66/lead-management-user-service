package com.registration.controller;


import com.registration.model.User;
import com.registration.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetCurrentUserWithOAuth2() throws Exception {
        // Mock user returned from service
        User mockUser = User.builder()
                .firstName("Ken")
                .lastName("Milota")
                .email("ken@example.com")
                .phoneNumber("555-1234")
                .address("123 Main St, USA")
                .companyName("Acme Corp")
                .jobTitle("Developer")
                .build();

        given(userService.getUserByEmail(anyString())).willReturn(Optional.of(mockUser));

        // OAuth2User attributes to simulate Google login
        Map<String, Object> attributes = Map.of(
                "email", "ken@example.com",
                "given_name", "Ken",
                "family_name", "Milota"
        );

        mockMvc.perform(get("/api/users/me")
                        .with(oauth2Login().attributes(a -> a.putAll(attributes))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ken"))
                .andExpect(jsonPath("$.lastName").value("Milota"))
                .andExpect(jsonPath("$.email").value("ken@example.com"))
                .andExpect(jsonPath("$.companyName").value("Acme Corp"));
    }

    @Test
    void testUpdateDetailsWithOAuth2() throws Exception {
        // Mock updated user returned from service
        User updatedUser = User.builder()
                .firstName("Ken")
                .lastName("Milota")
                .email("ken@example.com")
                .phoneNumber("555-1234")
                .address("123 Main St, USA")
                .companyName("Acme Corp")
                .jobTitle("Developer")
                .build();

        given(userService.updateAdditionalDetails(anyString(), any(User.class)))
                .willReturn(updatedUser);

        // OAuth2User attributes to simulate Google login
        Map<String, Object> attributes = Map.of(
                "email", "ken@example.com",
                "given_name", "Ken",
                "family_name", "Milota"
        );

        // JSON body for update
        String jsonBody = """
                {
                    "firstName": "Ken",
                    "lastName": "Milota",
                    "email": "ken@example.com",
                    "phoneNumber": "555-1234",
                    "address": "123 Main St, USA",
                    "companyName": "Acme Corp",
                    "jobTitle": "Developer"
                }
                """;

        mockMvc.perform(put("/api/users/details")
                        .with(oauth2Login().attributes(a -> a.putAll(attributes)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ken"))
                .andExpect(jsonPath("$.email").value("ken@example.com"));
    }
}