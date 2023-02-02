package com.example.carrental.controller;

import com.example.carrental.controller.aspects.Authorize;
import com.example.carrental.dto.RentalRequest;
import com.example.carrental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @PostMapping()
    @Authorize
    public ResponseEntity<?> rent(@RequestBody RentalRequest rentalRequest) {
        var response = rentalService.rent(rentalRequest);
        if (response.isError()) {
            var error = response.getError();
            return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    @Authorize(role = "Admin")
    public ResponseEntity<?> getRentals() {
        var rentals = rentalService.getRentals();
        if (rentals.isError()) {
            var error = rentals.getError();
            return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
        }

        return new ResponseEntity<>(rentals.getValue(), HttpStatus.OK);
    }
}
