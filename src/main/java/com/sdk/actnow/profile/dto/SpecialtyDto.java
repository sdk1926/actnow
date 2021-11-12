package com.sdk.actnow.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecialtyDto {

    private String speciaty;

    @Builder
    public SpecialtyDto(String speciaty){
        this.speciaty = speciaty;
    }
}
