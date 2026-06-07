package com.ecomerce.TechStore.user;

import java.time.LocalDateTime;

import com.ecomerce.TechStore.user.Role;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.ecomerce.TechStore.user.Role;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@AllArgsConstructor
@Builder
@Slf4j
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    private Role role = Role.ROLE_USER;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enabled;
    private boolean blocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
