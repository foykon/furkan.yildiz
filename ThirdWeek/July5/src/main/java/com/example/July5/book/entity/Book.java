package com.example.July5.book.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Book {
    private int id;
    private String title;
    private String author;
    private int pages;
}
