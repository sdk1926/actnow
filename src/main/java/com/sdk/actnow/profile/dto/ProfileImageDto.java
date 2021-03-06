package com.sdk.actnow.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileImageDto {

    private Long id;
    private String profileURL;

    @Builder
    public ProfileImageDto(
            Long id,
            String profileURL
    ){
        this.id = id;
        this.profileURL = profileURL;
    }
}
