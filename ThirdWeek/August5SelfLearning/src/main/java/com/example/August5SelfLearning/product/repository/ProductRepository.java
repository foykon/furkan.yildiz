package com.example.August5SelfLearning.product.repository;

import com.example.August5SelfLearning.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ProductRepository extends JpaRepository<Product, Integer> ,
        JpaSpecificationExecutor<Product> {



}
