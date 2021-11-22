package com.sdk.actnow.profile.domain;

import com.sdk.actnow.profile.dto.CareerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id",nullable = false)
    private Profile profile;

    @Column
    private int year;

    @Column
    private String name;

    @Column
    private String role;

    @Column
    private String category;

    @Builder
    public Career(Profile profile, int year, String name, String role, String category){
        this.profile = profile;
        this.year = year;
        this.name = name;
        this.role = role;
        this.category = category;
    }

    public void update(CareerDto careerDto) {
        this.year = careerDto.getYear();
        this.name = careerDto.getName();
        this.role = careerDto.getRole();
        this.category = careerDto.getCategory();
    }
}
