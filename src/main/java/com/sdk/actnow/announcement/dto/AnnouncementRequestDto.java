package com.sdk.actnow.announcement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.sdk.actnow.announcement.domain.Announcement;
import com.sdk.actnow.oauth.domain.users.Users;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class AnnouncementRequestDto {

    @NotBlank(message = "Title cannot be empty")
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

    @Builder
    public AnnouncementRequestDto(
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

    public Announcement toEntity(Users user){
        Announcement announcement = Announcement.builder()
                .user(user)
                .title(this.title)
                .producer(this.producer)
                .name(this.name)
                .kind(this.kind)
                .gender(this.gender)
                .directorName(this.directorName)
                .role(this.role)
                .age(this.age)
                .shootingPeriod(this.shootingPeriod)
                .pay(this.pay)
                .manager(this.manager)
                .email(this.email)
                .deadline(this.deadline)
                .details(this.details)
                .build();
        return announcement;
    }


}
