package com.example.July4.lombok;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor

@Builder
@ToString
@EqualsAndHashCode
public class Entity {
    private int id;
    private String name;


}
