package com.sdk.actnow.oauth.domain.users;

import com.sdk.actnow.oauth.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private long snsId;

    @Column(length = 200)
    private String email;

    @Builder
    public Users (long snsId, String email) {
        this.snsId = snsId;
        this.email = email;
    }
}
