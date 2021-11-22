package com.sdk.actnow.profile.domain;

import com.sdk.actnow.oauth.domain.BaseTimeEntity;
import com.sdk.actnow.profile.dto.SpecialtyRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Specialty extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Builder
    public Specialty(String name, Profile profile){
        this.name = name;
        this.profile = profile;
    }

    public void update(SpecialtyRequestDto specialtyRequestDto) {
        this.name = specialtyRequestDto.getSpecialty();
    }
}
