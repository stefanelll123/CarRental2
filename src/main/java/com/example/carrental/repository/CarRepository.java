package com.example.carrental.repository;

import com.example.carrental.entity.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CarRepository extends CrudRepository<Car, Integer> {
    CompletableFuture<List<Car>> findAllBy();
}
