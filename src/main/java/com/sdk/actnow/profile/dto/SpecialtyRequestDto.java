package com.sdk.actnow.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecialtyRequestDto {
    private String specialty;

    @Builder
    public SpecialtyRequestDto(String speciaty) {
        this.specialty = speciaty;
    }
}

