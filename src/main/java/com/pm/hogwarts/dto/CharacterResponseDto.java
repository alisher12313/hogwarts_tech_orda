package com.pm.hogwarts.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CharacterResponseDto {
    private String id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String house;
    private String patronus;
    private String image;
}
