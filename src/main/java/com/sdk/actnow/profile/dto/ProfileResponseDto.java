package com.sdk.actnow.profile.dto;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponseDto {

    private int year;
    private int age;
    private int height;
    private int weight;
    private String email;
    private String phoneNumber;
    private String snsAddress;
    private String aboutMe;
    private List<String> specialty;
//    private List<com.sdk.actnow.profile.dto.ProfileRequestDto.CareerDto> career;

    @Builder
    public ProfileResponseDto(
            int year,
            int age,
            int height,
            int weight,
            String email,
            String phoneNumber,
            String snsAddress,
            String aboutMe,
            List<String> specialty,
            List<com.sdk.actnow.profile.dto.ProfileRequestDto.CareerDto> career
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
    }

}



