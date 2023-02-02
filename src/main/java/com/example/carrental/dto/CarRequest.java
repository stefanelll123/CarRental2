package com.example.carrental.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

public final class CarRequest {
    @Getter
    @Setter
    @NotNull
    private String description;

    @Getter
    @Setter
    @NotNull
    private int modelId;
}
