package com.example.carrental.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "car")
public final class Car {
    @Id
    @GeneratedValue
    @Getter
    private int id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @Getter
    @Setter
    private Model model;

    public Car() {
    }

    public static Car create(String description, Model model) {
        var car = new Car();
        car.setDescription(description);
        car.setModel(model);

        return car;
    }
}
