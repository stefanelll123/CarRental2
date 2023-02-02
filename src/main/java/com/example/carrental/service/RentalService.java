package com.example.carrental.service;

import com.example.carrental.dto.RentalRequest;
import com.example.carrental.entity.Car;
import com.example.carrental.entity.Rental;
import com.example.carrental.internal.Result;
import com.example.carrental.internal.ResultError;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.RentalRepository;
import com.example.carrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@EnableAsync
public class RentalService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RentalRepository rentalRepository;

    public Result<List<Rental>> getRentals() {
        return Result.from(() -> rentalRepository.findAll());
    }

    @Async
    public Result rent(RentalRequest rentalRequest) {
        return Result.from(() -> rentalRepository.findByCar_IdAndEndDateAfter(rentalRequest.getCarId(), new Date()))
                .checkIf(List::isEmpty, new ResultError(HttpStatus.BAD_REQUEST, "Car already rented"))
                .onSuccess(response -> carRepository.findById(rentalRequest.getCarId()))
                .checkIf(Optional::isPresent, new ResultError(HttpStatus.BAD_REQUEST, "Car not found"))
                .onSuccess(Optional::get)
                .bind(car ->
                        Result.from(() -> userRepository.findById(rentalRequest.getUserId()))
                                .checkIf(Optional::isPresent, new ResultError(HttpStatus.BAD_REQUEST, "User not found"))
                                .onSuccess(Optional::get)
                                .onSuccess(user -> Rental.create(car, user, rentalRequest.getStartDate(),
                                        rentalRequest.getEndDate(), rentalRequest.getPrice()))
                                .onSuccess(rental -> rentalRepository.save(rental)));
    }
}
