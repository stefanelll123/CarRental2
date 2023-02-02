package com.example.carrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Getter;

import java.util.Date;

public final class RentalRequest {
    @Getter
    @NotNull
    private int carId;

    @Getter
    @NotNull
    private int userId;

    @Getter
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date startDate;

    @Getter
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date endDate;

    @Getter
    @NotNull
    private double price;
}
