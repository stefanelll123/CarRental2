package com.example.carrental.repository;

import com.example.carrental.entity.Model;
import org.springframework.data.repository.CrudRepository;

import java.util.concurrent.CompletableFuture;

public interface ModelRepository extends CrudRepository<Model, Integer> {
    CompletableFuture<Model> findById(int id);
}
