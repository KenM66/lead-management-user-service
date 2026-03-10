package com.users.controller;

import com.users.model.User;
import com.users.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PutMapping("/details")
    public User updateDetails(@AuthenticationPrincipal OAuth2User oauthUser,
                              @RequestBody User updatedUser) {

        String email = oauthUser.getAttribute("email");
        return service.updateAdditionalDetails(email, updatedUser);
    }
}
