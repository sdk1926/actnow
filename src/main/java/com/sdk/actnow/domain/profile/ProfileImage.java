package com.sdk.actnow.domain.profile;

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

    @Column(length = 512)
    private String profileName;

    @Column(length = 1024)
    private String profileURL;

    @Builder
    public ProfileImage(String profileName, String profileURL){
        this.profileName = profileName;
        this.profileURL = profileURL;
    }
}
