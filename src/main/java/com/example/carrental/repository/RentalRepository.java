package com.example.carrental.repository;

import com.example.carrental.entity.Car;
import com.example.carrental.entity.Rental;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface RentalRepository extends CrudRepository<Rental, Integer> {
    List<Rental> findAll();
    List<Rental> findByCar_IdAndEndDateAfter(int id, Date rentalStartDate);
}
