package com.sdk.actnow.domain.users;

import com.sdk.actnow.domain.BaseTimeEntity;
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

    @Column
    private int snsId;

    @Column(length = 200)
    private String email;

    @Builder
    public Users (int snsId, String email) {
        this.snsId = snsId;
        this.email = email;
    }
}
