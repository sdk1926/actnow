package com.sdk.actnow.announcement.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class AnnouncementRepositoryTest {

    @Autowired
    AnnouncementRepository announcementRepository;

    @AfterEach
    public void cleanup() {announcementRepository.deleteAllInBatch();}

    @DisplayName("공고저장_불러오기")
    @Test
    public void saveAnnouncementRead() {
        //given
        String name = "풀이 나지 않는 땅";
        String kind = "단편 영화";
        String directorName = "강소현";
        String role = "샴푸의 요정";
        String age = "20대";
        String shootingPeriod = "2주일";
        String pay = "협의";
        String manager = "서동규";
        String email = "sdk1926";
        LocalDate deadline = LocalDate.of(2021,12,30);
        String details = "많은 지원!";

        announcementRepository.save(Announcement.builder()
                .name(name)
                .kind(kind)
                .directorName(directorName)
                .role(role)
                .age(age)
                .shootingPeriod(shootingPeriod)
                .pay(pay)
                .manager(manager)
                .email(email)
                .deadline(deadline)
                .details(details)
                .build());

        // when
        List<Announcement> announcementList = announcementRepository.findAll();

        //then
        Announcement announcement = announcementList.get(0);
        assertThat(announcement.getEmail()).isEqualTo(email);
        assertThat(announcement.getPay()).isEqualTo(pay);
    }
}