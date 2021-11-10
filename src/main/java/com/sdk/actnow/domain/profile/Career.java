package com.sdk.actnow.domain.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "profile_id")
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
}
