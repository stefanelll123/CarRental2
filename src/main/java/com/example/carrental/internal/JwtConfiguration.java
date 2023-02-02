package com.example.carrental.internal;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public final class JwtConfiguration {
    @Value("${jwt.secret}")
    @Getter
    private String secret;
}
