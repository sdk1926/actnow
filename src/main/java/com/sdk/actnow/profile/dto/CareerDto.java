package com.sdk.actnow.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CareerDto {
    int year;
    String name;
    String role;
    String category;

    @Builder
    public CareerDto(String category, int year, String name, String role){
        this.year = year;
        this.name = name;
        this.role = role;
        this.category= category;
    }
}
