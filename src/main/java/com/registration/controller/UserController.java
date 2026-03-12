package com.registration.controller;

import com.registration.model.User;
import com.registration.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // GET /api/users/me → fetch current user details
    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        return service.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // PUT /api/users/details → update user details
    @PutMapping("/details")
    public User updateDetails(@AuthenticationPrincipal OAuth2User oauthUser,
                              @RequestBody User updatedUser) {
        String email = oauthUser.getAttribute("email");
        return service.updateAdditionalDetails(email, updatedUser);
    }
}