package com.example.carrental.dto;

import lombok.Getter;

public final class TokenResponse {
    @Getter
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }
}
