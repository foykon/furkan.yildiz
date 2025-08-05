package com.example.August5SelfLearning.product.service;

import com.example.August5SelfLearning.product.dto.ProductFilterRequest;
import com.example.August5SelfLearning.product.dto.ProductPageResponse;
import com.example.August5SelfLearning.product.dto.ProductRequest;
import com.example.August5SelfLearning.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse getProductById(int id);
    ProductResponse updateProduct(int id, ProductRequest productRequest);
    void deleteProduct(int id);
    List<ProductResponse> getAllProducts();
    ProductPageResponse getFilteredProducts(ProductFilterRequest request);
}
