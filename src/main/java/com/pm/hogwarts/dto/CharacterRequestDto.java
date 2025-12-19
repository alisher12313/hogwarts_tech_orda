package com.pm.hogwarts.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CharacterRequestDto {
    private String id;
    private String name;
    private String house;
    private String patronus;
    private String image;
}
