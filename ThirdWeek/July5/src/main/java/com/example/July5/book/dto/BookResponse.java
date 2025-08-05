package com.example.July5.book.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private int id;
    private String title;
    private String author;
    private int pages;
}
