package com.registration.config;

import com.registration.model.User;
import com.registration.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // Map Google attributes to your User class
        User user = new User();
        user.setEmail(oauthUser.getAttribute("email"));
        user.setFirstName(oauthUser.getAttribute("given_name")); // Google attribute
        user.setLastName(oauthUser.getAttribute("family_name")); // Google attribute

        // Optional: you can leave other fields blank, or set defaults
        user.setPhoneNumber("");
        user.setAddress("");
        user.setCompanyName("");
        user.setJobTitle("");

        // Register or update user
        userService.registerOrUpdateUser(user);

        // Redirect to homepage or dashboard
        response.sendRedirect("/");
    }
}
