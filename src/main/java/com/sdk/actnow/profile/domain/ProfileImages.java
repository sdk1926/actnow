package com.sdk.actnow.profile.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProfileImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(length = 1024)
    private String profileURL;

    @Builder
    public ProfileImages(Profile profile, String profileURL){
        this.profile = profile;
        this.profileURL = profileURL;
    }
}
