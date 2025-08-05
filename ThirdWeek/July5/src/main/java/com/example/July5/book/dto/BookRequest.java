package com.example.July5.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class BookRequest {
        @NotBlank
        private String title;
        private String author;
        private int pages;
}
