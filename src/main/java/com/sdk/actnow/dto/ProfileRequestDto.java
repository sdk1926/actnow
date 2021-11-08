package com.sdk.actnow.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequestDto {

    private int year;
    private int age;
    private int height;
    private int weight;
    private String email;
    private String phoneNumber;
    private String snsAddress;
    private String aboutMe;
    private List<String> specialty;
    private CategoryDto categoryDto;
    private CareerDto careerDto;

    @Builder
    public ProfileRequestDto(
            int year,
            int age,
            int height,
            int weight,
            String email,
            String phoneNumber,
            String snsAddress,
            String aboutMe,
            List<String> specialty,
            CategoryDto categoryDto,
            CareerDto careerDto
    ){
        this.year = year;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.snsAddress = snsAddress;
        this.aboutMe = aboutMe;
        this.specialty = specialty;
        this.categoryDto = categoryDto;
        this.careerDto = careerDto;
    }

    @Getter
    public static class CategoryDto{
        String name;

        @Builder
        public CategoryDto(String name){
            this.name = name;
        }
    }

    @Getter
    public static class CareerDto{
        int year;
        String name;
        String role;
        CategoryDto categoryDto;

        @Builder
        public CareerDto(int year, String name, String role, CategoryDto categoryDto){
            this.year = year;
            this.name = name;
            this.role = role;
            this.categoryDto = categoryDto;
        }
    }
}
