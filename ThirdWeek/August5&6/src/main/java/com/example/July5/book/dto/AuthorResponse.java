package com.example.July5.book.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuthorResponse {
    private int id;
    private String name;
    private String surname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
