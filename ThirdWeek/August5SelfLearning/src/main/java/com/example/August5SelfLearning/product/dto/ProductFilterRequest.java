package com.example.August5SelfLearning.product.dto;

import lombok.Data;

@Data
public class ProductFilterRequest {
    // Filtering
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private Integer minStock;
    private Integer maxStock;

    // Pagination
    private int page = 0;
    private int size = 10;

    // Sorting
    private String sortBy = "id";
    private String sortDir = "asc";
}
