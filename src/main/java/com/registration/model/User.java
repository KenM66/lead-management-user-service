package com.registration.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class User {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;

    private String phoneNumber;
    private String address;
    private String companyName;
    private String jobTitle;
}