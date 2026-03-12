package com.registration.security;

import com.registration.config.OAuth2LoginSuccessHandler;
import com.registration.model.User;
import com.registration.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OAuth2LoginSuccessHandlerTest {

    private UserService userService;
    private OAuth2LoginSuccessHandler handler;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Authentication authentication;
    private OAuth2User oauthUser;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        handler = new OAuth2LoginSuccessHandler(userService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authentication = mock(Authentication.class);
        oauthUser = mock(OAuth2User.class);
    }

    @Test
    void testRegisterUserFromGoogleLogin() throws Exception {

        // Mock Google attributes
        when(oauthUser.getAttribute("email")).thenReturn("ken@example.com");
        when(oauthUser.getAttribute("given_name")).thenReturn("Ken");
        when(oauthUser.getAttribute("family_name")).thenReturn("Milota");

        when(authentication.getPrincipal()).thenReturn(oauthUser);

        User savedUser = new User();
        savedUser.setEmail("ken@example.com");
        savedUser.setFirstName("Ken");
        savedUser.setLastName("Milota");

        when(userService.registerOrUpdateUser(any(User.class))).thenReturn(savedUser);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(userService, times(1)).registerOrUpdateUser(any(User.class));
        verify(response).sendRedirect("/");
    }
}