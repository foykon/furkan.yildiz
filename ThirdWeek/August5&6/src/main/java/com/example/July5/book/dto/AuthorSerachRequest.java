package com.example.July5.book.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorSerachRequest {
    private String name;
    private String surname;

}
