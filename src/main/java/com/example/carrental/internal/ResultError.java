package com.example.carrental.internal;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public final class ResultError {
    @Getter
    private HttpStatus statusCode;

    @Getter
    private String message;

    public ResultError(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ResultError(HttpStatus statusCode) {
        this.statusCode = statusCode;
        this.message = "";
    }

    public ResultError(String message) {
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public ResultError() {
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = "";
    }
}
