package com.sdk.actnow.dto;

import com.sdk.actnow.domain.profile.Profile;
import com.sdk.actnow.domain.profile.Specialty;
import com.sdk.actnow.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    public Profile toEntity(Users user) {
        Profile entity = Profile.builder()
                .user(user)
                .age(this.age)
                .height(this.height)
                .weight(this.weight)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .snsAddress(this.snsAddress)
                .aboutMe(this.aboutMe)
                .build();
        return entity;
    }

    public List<Specialty> toEntity(Profile profile) {
        List<Specialty> entity = new ArrayList<>();
        for(String s:this.specialty){
            Specialty specialty = Specialty.builder()
                    .profile(profile)
                    .name(s)
                    .build();
            entity.add(specialty);
        }
        return entity;
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
