package com.example.carrental.controller;

import com.example.carrental.dto.UserRequest;
import com.example.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        var response = userService.login(userRequest);
        if (response.isError()) {
            var error = response.getError();
            return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
        }

        return new ResponseEntity<>(response.getValue(), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        var response = userService.register(userRequest);
        if (response.isError()) {
            var error = response.getError();
            return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}