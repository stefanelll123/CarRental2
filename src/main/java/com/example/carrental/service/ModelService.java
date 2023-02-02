package com.example.carrental.service;

import com.example.carrental.entity.Model;
import com.example.carrental.internal.Result;
import com.example.carrental.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ModelService {
    @Autowired
    private ModelRepository modelRepository;

    public Result<List<Model>> getAllModels() {
        return Result.from(() -> modelRepository.findAll())
                .onSuccess(models -> StreamSupport
                        .stream(models.spliterator(), false)
                        .collect(Collectors.toList()));
    }

    public Result addModel(Model model){
        return Result.from(() -> modelRepository.save(model));
    }
}

