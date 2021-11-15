package com.sdk.actnow.profile.dto;
import com.sdk.actnow.profile.domain.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileResponseDto {

    private int age;
    private String name;
    private int height;
    private int weight;
    private String email;
    private String phoneNumber;
    private String snsAddress;
    private String aboutMe;
    private String profileImage;
    private List<String> profileImages = new ArrayList<>();
    private List<String> specialty = new ArrayList<>();
    private List<CareerDto> career = new ArrayList<>();

    @Builder
    public ProfileResponseDto(Profile profile,
                              List<Specialty> specialties,
                              List<Career> careers
                              ){
        this.age = profile.getAge();
        this.name = profile.getName();
        this.height = profile.getHeight();
        this.weight = profile.getWeight();
        this.email = profile.getEmail();
        this.phoneNumber = profile.getPhoneNumber();
        this.snsAddress = profile.getSnsAddress();
        this.aboutMe = profile.getAboutMe();
        if (!specialties.isEmpty()){
            for(Specialty s:specialties){
                this.specialty.add(s.getName());
            }
        }
        if (!careers.isEmpty()){
            for(Career c:careers) {
                this.career.add(CareerDto.builder()
                        .name(c.getName())
                        .role(c.getCategory())
                        .category(c.getCategory())
                        .year(c.getYear())
                        .build());
            }
        }
        if(!(profile.getProfileImage() == null)){
                this.profileImage = profile.getProfileImage().getProfileURL();
            }
        if(!profile.getProfileImages().isEmpty()){
            for (ProfileImages p: profile.getProfileImages()){
                profileImages.add(p.getProfileURL());
            }
        }
        }

    }




