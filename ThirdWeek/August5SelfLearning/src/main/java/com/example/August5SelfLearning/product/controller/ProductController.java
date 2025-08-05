package com.example.August5SelfLearning.product.controller;


import com.example.August5SelfLearning.product.dto.ProductFilterRequest;
import com.example.August5SelfLearning.product.dto.ProductPageResponse;
import com.example.August5SelfLearning.product.dto.ProductRequest;
import com.example.August5SelfLearning.product.dto.ProductResponse;
import com.example.August5SelfLearning.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*@GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }*/

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {


        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable int id, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @GetMapping
    public ProductPageResponse getFilteredProducts(ProductFilterRequest request) {
        return productService.getFilteredProducts(request);
    }

}
