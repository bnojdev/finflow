package com.binoj.finflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String mobile;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(length = 6)
    private String otp;
}
