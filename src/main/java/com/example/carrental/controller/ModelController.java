package com.example.carrental.controller;

import com.example.carrental.controller.aspects.Authorize;
import com.example.carrental.entity.Model;
import com.example.carrental.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models")
public class ModelController {
    @Autowired
    private ModelService modelService;

    @GetMapping()
    public ResponseEntity<?> getAllModels() {
        var models = modelService.getAllModels();
        if (models.isError()) {
            var error = models.getError();
            return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
        }

        return new ResponseEntity<>(models.getValue(), HttpStatus.OK);
    }

    @PostMapping()
    @Authorize
    public ResponseEntity<?> addModel(@RequestBody Model model) {
        var response = modelService.addModel(model);
        if (response.isError()) {
            var error = response.getError();
            return new ResponseEntity<>(error.getMessage(), error.getStatusCode());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
