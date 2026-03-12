package com.registration.service;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Fetch a user by email.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Update existing user details.
     */
    public User updateAdditionalDetails(String email, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findByEmail(email);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setCompanyName(updatedUser.getCompanyName());
            existingUser.setJobTitle(updatedUser.getJobTitle());
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    /**
     * Register a new user or update an existing user based on Google login info.
     * If the user already exists, updates firstName and lastName.
     */
    public User registerOrUpdateUser(User userFromOAuth) {
        Optional<User> existingUserOpt = userRepository.findByEmail(userFromOAuth.getEmail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            // Update basic info from Google OAuth
            existingUser.setFirstName(userFromOAuth.getFirstName());
            existingUser.setLastName(userFromOAuth.getLastName());
            return userRepository.save(existingUser);
        } else {
            // New user registration
            return userRepository.save(userFromOAuth);
        }
    }
}