package com.sdk.actnow.domain.profile;

import com.sdk.actnow.domain.BaseTimeEntity;
import com.sdk.actnow.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Profile extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column
    private int age;

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

    @Builder
    public Profile(
            Users user,
            int age,
            int height,
            int weight,
            String email,
            String phoneNumber,
            String snsAddress,
            String aboutMe
    ){
        this.user = user;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.snsAddress = snsAddress;
        this.aboutMe = aboutMe;
    }
}
