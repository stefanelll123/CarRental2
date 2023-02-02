package com.example.carrental.controller;

import com.example.carrental.controller.aspects.Authorize;
import com.example.carrental.dto.CarRequest;
import com.example.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cars")
@EnableAsync
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping()
    @Async()
    public CompletableFuture<ResponseEntity<?>> getAllCars() {
        return carService.getAllCars()
                .thenApply(cars -> {
                    if (cars.isError()) {
                        var error = cars.getError();
                        return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
                    }

                    return new ResponseEntity<>(cars.getValue(), HttpStatus.OK);
                });
    }

    @PostMapping()
    // @Authorize
    @Async()
    public CompletableFuture<ResponseEntity> addCar(@RequestBody CarRequest carRequest) {
         return carService.addCar(carRequest)
                .thenApply(response -> {
                    if (response.isError()) {
                        var error = response.getError();
                        return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
                    }

                    return new ResponseEntity<>(HttpStatus.OK);
                });

    }
}
