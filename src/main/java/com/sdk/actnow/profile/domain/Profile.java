package com.sdk.actnow.profile.domain;

import com.sdk.actnow.oauth.domain.BaseTimeEntity;
import com.sdk.actnow.oauth.domain.users.Users;
import com.sdk.actnow.profile.dto.ProfileRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Profile extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    @Column
    private int age;

    @Column
    private String name;

    @Column
    private int height;

    @Column
    private int weight;

    @Column
    private  String email;

    @Column
    private String phoneNumber;

    @Column
    private String snsAddress;

    @Column(length = 2048)
    private String aboutMe;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "profile")
    private List<Career> careers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "profile")
    private List<Specialty> specialties;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "profile")
    private ProfileImage profileImage;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "profile")
    private List<ProfileImages> profileImages;

    @Builder
    public Profile(
            Users user,
            int age,
            String name,
            int height,
            int weight,
            String email,
            String phoneNumber,
            String snsAddress,
            String aboutMe,
            ProfileImage profileImage,
            List<ProfileImages> profileImages
    ){
        this.user = user;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.snsAddress = snsAddress;
        this.aboutMe = aboutMe;
        this.profileImage = profileImage;
        this.profileImages = profileImages;
    }

    public void update(ProfileRequestDto profileRequestDto) {
        this.age = profileRequestDto.getAge();
        this.name = profileRequestDto.getName();
        this.height = profileRequestDto.getHeight();
        this.weight = profileRequestDto.getWeight();
        this.email = profileRequestDto.getEmail();
        this.phoneNumber = profileRequestDto.getPhoneNumber();
        this.snsAddress = profileRequestDto.getSnsAddress();
        this.aboutMe = profileRequestDto.getAboutMe();
    }
}
