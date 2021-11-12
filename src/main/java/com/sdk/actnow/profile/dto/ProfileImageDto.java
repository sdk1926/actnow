package com.sdk.actnow.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileImageDto {

    private String profileURL;

    @Builder
    public ProfileImageDto(
            String profileURL
    ){
        this.profileURL = profileURL;
    }
}
