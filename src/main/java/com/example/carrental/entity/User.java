package com.example.carrental.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.management.relation.Role;
import javax.persistence.*;

@Entity
@Table(name = "user")
public final class User {
    @Id
    @GeneratedValue
    @Getter
    private int id;

    @Getter
    @Setter
    @Column(unique=true, nullable = false)
    private String email;

    @Getter
    @Setter
    @NotNull
    private String passwordHash;

    @Getter
    @Setter
    @NotNull
    private RoleEnum role;

    public static User create(String email, String passwordHash, RoleEnum role) {
        var user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(role);

        return user;
    }
}
