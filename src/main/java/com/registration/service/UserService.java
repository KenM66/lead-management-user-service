package com.registration.service;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User registerOrUpdateUser(Map<String, Object> googleAttributes) {

        String email = (String) googleAttributes.get("email");
        String fullName = (String) googleAttributes.get("name");

        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ").length > 1 ? fullName.split(" ")[1] : "";

        return repository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    return repository.save(user);
                });
    }

    public User updateAdditionalDetails(String email, User updatedDetails) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhoneNumber(updatedDetails.getPhoneNumber());
        user.setAddress(updatedDetails.getAddress());
        user.setCompanyName(updatedDetails.getCompanyName());
        user.setJobTitle(updatedDetails.getJobTitle());

        return repository.save(user);
    }
}
