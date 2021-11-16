package com.sdk.actnow.profile.dto;
import com.sdk.actnow.profile.domain.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileResponseDto {

    private Long id;
    private int age;
    private String name;
    private int height;
    private int weight;
    private String email;
    private String phoneNumber;
    private String snsAddress;
    private String aboutMe;
    private ProfileImageDto profileImage;
    private List<ProfileImageDto> profileImages = new ArrayList<>();
    private List<SpecialtyDto> specialty = new ArrayList<>();
    private List<CareerDto> career = new ArrayList<>();

    @Builder
    public ProfileResponseDto(Profile profile,
                              List<Specialty> specialties,
                              List<Career> careers
                              ){
        this.id = profile.getId();
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
                this.specialty.add(SpecialtyDto.builder()
                        .id(s.getId())
                        .speciaty(s.getName())
                        .build()
                );
            }
        }
        if (!careers.isEmpty()){
            for(Career c:careers) {
                this.career.add(CareerDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .role(c.getCategory())
                        .category(c.getCategory())
                        .year(c.getYear())
                        .build());
            }
        }
        if(!(profile.getProfileImage() == null)){
                this.profileImage = ProfileImageDto.builder()
                        .id(profile.getProfileImage().getId())
                        .profileURL(profile.getProfileImage().getProfileURL())
                        .build();
            }
        if(!profile.getProfileImages().isEmpty()){
            for (ProfileImages p: profile.getProfileImages()){
                profileImages.add(ProfileImageDto.builder()
                .id(p.getId())
                .profileURL(p.getProfileURL())
                .build());
            }
        }
        }

    }




