package com.example.carrental;

import com.example.carrental.dto.CarRequest;
import com.example.carrental.entity.Model;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.ModelRepository;
import com.example.carrental.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CarServiceTests {
    @Mock
    private CarRepository carRepository;

    @Mock
    private ModelRepository modelRepository;

    @Test
    public void shouldAddCarWhenAddCarAndModelExists() throws ExecutionException, InterruptedException {
        // arrange
        var carRequest = new CarRequest();
        carRequest.setDescription("A nice car");
        carRequest.setModelId(1);

        Mockito.when(carRepository.save(any()))
                .thenReturn(null);
        Mockito.when(modelRepository.findById(carRequest.getModelId()))
                .thenReturn(CompletableFuture.supplyAsync(Model::new));

        var sut = new CarService();
        ReflectionTestUtils.setField(sut, "carRepository", carRepository);
        ReflectionTestUtils.setField(sut, "modelRepository", modelRepository);

        // act
        var result = sut.addCar(carRequest).get();

        // assert
        Assert.isTrue(result.isSuccessful(), "Could not add the car");
    }


    @Test
    public void shouldNotAddCarWhenAddCarAndModelDoesNotExists() throws ExecutionException, InterruptedException {
        // arrange
        var carRequest = new CarRequest();
        carRequest.setDescription("A nice car");
        carRequest.setModelId(1);

        Mockito.when(carRepository.save(any()))
                .thenReturn(null);
        Mockito.when(modelRepository.findById(carRequest.getModelId()))
                .thenReturn(CompletableFuture.supplyAsync(() -> null));

        var sut = new CarService();
        ReflectionTestUtils.setField(sut, "carRepository", carRepository);
        ReflectionTestUtils.setField(sut, "modelRepository", modelRepository);

        // act
        var result = sut.addCar(carRequest).get();

        // assert
        Assert.isTrue(result.isError(), "Result finished without error");
    }
}
