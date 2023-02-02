package com.example.carrental.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public final class UserRequest {
    @Getter
    @Setter
    @NotNull
    private String email;

    @Getter
    @Setter
    @NotNull
    private String password;

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
