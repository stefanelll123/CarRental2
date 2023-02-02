package com.example.carrental.service;

import com.example.carrental.dto.CarRequest;
import com.example.carrental.entity.Car;
import com.example.carrental.internal.Result;
import com.example.carrental.internal.ResultError;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ModelRepository modelRepository;

    public CompletableFuture<Result<List<Car>>> getAllCars(){
        return carRepository.findAllBy()
                .thenApply(Result::ok)
                .exceptionally(error -> Result.error(new ResultError()));
    }

    public CompletableFuture<Result> addCar(CarRequest carRequest) {
        return modelRepository.findById(carRequest.getModelId())
                .thenApply(model ->
                        Result.ok(model)
                        .checkIf(Objects::nonNull, new ResultError(HttpStatus.BAD_REQUEST, "Invalid model"))
                        .onSuccess(m -> Car.create(carRequest.getDescription(), m))
                        .onSuccess(car -> carRepository.save(car)));
    }
}
