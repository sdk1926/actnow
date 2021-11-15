package com.sdk.actnow.profile.dto;

import com.sdk.actnow.profile.domain.Profile;
import com.sdk.actnow.profile.domain.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileListResponseDto {

    private Long id;
    private int age;
    private String name;
    private int height;
    private int weight;
    private String email;
    private String profileImage;

    @Builder
    public ProfileListResponseDto(Profile profile
    ) {
        this.id = profile.getId();
        this.age = profile.getAge();
        this.name = profile.getName();
        this.height = profile.getHeight();
        this.weight = profile.getWeight();
        this.email = profile.getEmail();
        if(!(profile.getProfileImage() == null)){
            this.profileImage = profile.getProfileImage().getProfileURL();
        }
    }
}
