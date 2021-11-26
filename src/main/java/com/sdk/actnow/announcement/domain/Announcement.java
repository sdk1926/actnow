package com.sdk.actnow.announcement.domain;

import com.sdk.actnow.announcement.dto.AnnouncementRequestDto;
import com.sdk.actnow.oauth.domain.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column
    private String title;

    @Column
    private String producer;

    @Column(length = 256)
    private String name;

    @Column(length = 128)
    private String kind;

    @Column(length = 64)
    private String directorName;

    @Column(length = 64)
    private String role;

    @Column(length = 64)
    private String gender;

    @Column
    private String age;

    @Column
    private String shootingPeriod;

    @Column
    private String pay;

    @Column
    private String maanger;

    @Column
    private String email;

    @Column
    private LocalDate deadline;

    @Column(length = 4000)
    private String details;

    @Builder
    public Announcement(
            Users user,
            String title,
            String name,
            String kind,
            String directorName,
            String role,
            String age,
            String shootingPeriod,
            String pay,
            String producer,
            String manager,
            String email,
            String gender,
            LocalDate deadline,
            String details
    ){
        this.user = user;
        this.title = title;
        this.producer = producer;
        this.name = name;
        this.kind = kind;
        this.directorName = directorName;
        this.role = role;
        this.age = age;
        this.shootingPeriod = shootingPeriod;
        this.pay = pay;
        this.maanger = manager;
        this.email = email;
        this.gender = gender;
        this.deadline = deadline;
        this.details = details;
    }

    public void update(AnnouncementRequestDto announcementRequestDto){
        this.name = announcementRequestDto.getName();
        this.producer = announcementRequestDto.getProducer();
        this.title = announcementRequestDto.getTitle();
        this.kind = announcementRequestDto.getKind();
        this.directorName = announcementRequestDto.getDirectorName();
        this.role = announcementRequestDto.getRole();
        this.age  = announcementRequestDto.getAge();
        this.shootingPeriod = announcementRequestDto.getShootingPeriod();
        this.pay = announcementRequestDto.getPay();
        this.maanger = announcementRequestDto.getManager();
        this.email = announcementRequestDto.getEmail();
        this.gender = announcementRequestDto.getGender();
        this.deadline = announcementRequestDto.getDeadline();
        this.details = announcementRequestDto.getDetails();
    }
}
