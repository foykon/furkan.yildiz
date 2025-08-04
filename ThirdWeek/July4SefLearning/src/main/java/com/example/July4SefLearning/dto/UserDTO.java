package com.example.July4SefLearning.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class UserDTO {
    private int id;
    private String name;
}
