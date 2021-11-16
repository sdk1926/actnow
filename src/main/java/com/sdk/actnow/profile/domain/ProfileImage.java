package com.sdk.actnow.profile.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "profile_id", unique = true, nullable = false)
    private Profile profile;

    @Column(length = 1024)
    private String profileURL;

    @Builder
    public ProfileImage(Profile profile, String profileURL){
        this.profile = profile;
        this.profileURL = profileURL;
    }

    public void update(String url) {
        this.profileURL = url;
    }
}
