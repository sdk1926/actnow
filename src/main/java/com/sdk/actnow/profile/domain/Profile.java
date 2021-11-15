package com.sdk.actnow.profile.domain;

import com.sdk.actnow.domain.BaseTimeEntity;
import com.sdk.actnow.domain.users.Users;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
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

}
