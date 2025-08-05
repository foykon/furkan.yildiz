package com.example.August5SelfLearning.product.service.impl;

import com.example.August5SelfLearning.product.dto.ProductFilterRequest;
import com.example.August5SelfLearning.product.dto.ProductPageResponse;
import com.example.August5SelfLearning.product.dto.ProductRequest;
import com.example.August5SelfLearning.product.dto.ProductResponse;
import com.example.August5SelfLearning.product.entity.Product;
import com.example.August5SelfLearning.product.filter.ProductSpecifications;
import com.example.August5SelfLearning.product.repository.ProductRepository;
import com.example.August5SelfLearning.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapRequestToProduct(productRequest);
        productRepository.save(product);
        return mapProductToResponse(product);
    }

    @Override
    public ProductResponse getProductById(int id) {
        Product product = productRepository.findById(id).get();

        return mapProductToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(int id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).get();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        productRepository.save(product);


        return mapProductToResponse(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapProductToResponse).toList();
    }

    @Override
    public ProductPageResponse getFilteredProducts(ProductFilterRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy())
        );

        Specification<Product> spec = null;

        if (request.getName() != null)
            spec = (spec == null) ? ProductSpecifications.hasName(request.getName()) : spec.and(ProductSpecifications.hasName(request.getName()));

        if (request.getMinPrice() != null)
            spec = (spec == null) ? ProductSpecifications.priceGreaterThan(request.getMinPrice()) : spec.and(ProductSpecifications.priceGreaterThan(request.getMinPrice()));

        if (request.getMaxPrice() != null)
            spec = (spec == null) ? ProductSpecifications.priceLessThan(request.getMaxPrice()) : spec.and(ProductSpecifications.priceLessThan(request.getMaxPrice()));

        if (request.getMinStock() != null)
            spec = (spec == null) ? ProductSpecifications.stockGreaterThan(request.getMinStock()) : spec.and(ProductSpecifications.stockGreaterThan(request.getMinStock()));

        if (request.getMaxStock() != null)
            spec = (spec == null) ? ProductSpecifications.stockLessThan(request.getMaxStock()) : spec.and(ProductSpecifications.stockLessThan(request.getMaxStock()));


        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<ProductResponse> productResponses = productPage
                .getContent()
                .stream()
                .map(this::mapProductToResponse)
                .toList();

        return new ProductPageResponse(
                productResponses,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    private ProductResponse mapProductToResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    private Product mapRequestToProduct(ProductRequest productRequest){
        return Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .build();
    }
}
