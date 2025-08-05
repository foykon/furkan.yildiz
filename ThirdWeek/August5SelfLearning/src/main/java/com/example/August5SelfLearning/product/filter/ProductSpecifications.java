package com.example.August5SelfLearning.product.filter;

import com.example.August5SelfLearning.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> priceGreaterThan(Double min) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<Product> priceLessThan(Double max) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("price"), max);
    }

    public static Specification<Product> stockGreaterThan(Integer min) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("stock"), min);
    }

    public static Specification<Product> stockLessThan(Integer max) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("stock"), max);
    }
}
