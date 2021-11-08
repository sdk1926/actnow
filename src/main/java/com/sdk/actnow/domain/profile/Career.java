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

    @Column
    private int year;

    @Column
    private String name;

    @Column
    private String role;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Career(int year, String name, String role, Category category){
        this.year = year;
        this.name = name;
        this.role = role;
        this.category = category;
    }
}
