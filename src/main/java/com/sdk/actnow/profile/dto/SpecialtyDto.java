package com.sdk.actnow.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecialtyDto {
    private Long id;
    private String speciaty;

    @Builder
    public SpecialtyDto(long id, String speciaty) {
        this.id = id;
        this.speciaty = speciaty;
    }
}
