package com.example.August5SelfLearning.product.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@SuperBuilder
public class Product extends BaseEntity {
    @Column
    private String name;
    @Column
    private double price;
    @Column
    private int stock;

}
