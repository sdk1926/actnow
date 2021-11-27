package com.sdk.actnow.announcement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.sdk.actnow.announcement.domain.Announcement;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementResponseDto {

    private Long id;
    private String title;
    private String producer;
    private String name;
    private String kind;
    private String directorName;
    private String role;
    private String age;
    private String shootingPeriod;
    private String pay;
    private String manager;
    private String email;
    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate deadline;
    private String details;
    private boolean isMine;

    @Builder
    public AnnouncementResponseDto(
            Long id,
            String title,
            String producer,
            String name,
            String kind,
            String directorName,
            String role,
            String age,
            String shootingPeriod,
            String pay,
            String manager,
            String email,
            String gender,
            LocalDate deadline,
            String details
    ){
        this.id = id;
        this.title = title;
        this.producer = producer;
        this.name = name;
        this.kind = kind;
        this.directorName = directorName;
        this.role = role;
        this.age = age;
        this.shootingPeriod = shootingPeriod;
        this.pay = pay;
        this.manager = manager;
        this.email = email;
        this.gender = gender;
        this.deadline = deadline;
        this.details = details;
    }

    public AnnouncementResponseDto(Announcement announcement){
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.producer = announcement.getProducer();
        this.name = announcement.getName();
        this.kind = announcement.getKind();
        this.directorName = announcement.getDirectorName();
        this.role = announcement.getRole();
        this.age = announcement.getAge();
        this.shootingPeriod = announcement.getShootingPeriod();
        this.pay = announcement.getPay();
        this.manager = announcement.getMaanger();
        this.email = announcement.getEmail();
        this.gender = announcement.getGender();
        this.deadline = announcement.getDeadline();
        this.details = announcement.getDetails();
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", producer='" + producer + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", directorName='" + directorName + '\'' +
                ", role='" + role + '\'' +
                ", age='" + age + '\'' +
                ", shootingPeriod='" + shootingPeriod + '\'' +
                ", pay='" + pay + '\'' +
                ", manager='" + manager + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", deadline=" + deadline +
                ", details='" + details + '\'' +
                '}';
    }
}
