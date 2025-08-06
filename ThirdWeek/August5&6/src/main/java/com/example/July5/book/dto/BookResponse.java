package com.example.July5.book.dto;

import com.example.July5.book.entity.Author;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private int id;
    private String title;
    private Author author;
    private int pages;
}
