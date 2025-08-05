package com.example.August5SelfLearning.product.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private int id;
    private String name;
    private double price;
    private int stock;
}
