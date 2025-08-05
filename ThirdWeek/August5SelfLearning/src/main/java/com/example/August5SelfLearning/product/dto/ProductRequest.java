package com.example.August5SelfLearning.product.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank
    private String name;
    @NotNull
    @PositiveOrZero
    private double price;
    @NotNull
    @Min(0)
    private int stock;
}
