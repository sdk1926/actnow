package com.sdk.actnow.profile.dto;
import com.sdk.actnow.profile.domain.Career;
import com.sdk.actnow.profile.domain.Profile;
import com.sdk.actnow.profile.domain.ProfileImage;
import com.sdk.actnow.profile.domain.Specialty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileResponseDto {

    private int age;
    private int height;
    private int weight;
    private String email;
    private String phoneNumber;
    private String snsAddress;
    private String aboutMe;
    private List<String> specialty = new ArrayList<>();
    private List<CareerDto> career = new ArrayList<>();
    private List<String> profileImage = new ArrayList<>();

    @Builder
    public ProfileResponseDto(Profile profile,
                              List<Specialty> specialties,
                              List<Career> careers,
                              List<ProfileImage> profileImageList){
        this.age = profile.getAge();
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
        if(!profileImageList.isEmpty()){
            for(ProfileImage p:profileImageList){
                this.profileImage.add(p.getProfileURL());
            }
        }
    }

}



