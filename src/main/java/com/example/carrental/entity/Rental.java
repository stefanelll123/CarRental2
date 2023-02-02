package com.example.carrental.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rental")
public class Rental {
    @Id
    @GeneratedValue
    @Getter
    private int id;

    @ManyToOne(optional = false)
    @Getter
    @Setter
    private Car car;

    @ManyToOne(optional = false)
    @Getter
    @Setter
    private User user;

    @Getter
    @Column(nullable = false)
    private Date startDate;

    @Getter
    @NotNull
    @Column(nullable = false)
    private Date endDate;

    @Getter
    @NotNull
    @Column(nullable = false)
    private double price;

    public static Rental create(Car car, User user, Date startDate, Date endDate, double price) {
        var rental = new Rental();
        rental.car = car;
        rental.user = user;
        rental.startDate = startDate;
        rental.endDate = endDate;
        rental.price = price;

        return rental;
    }
}
