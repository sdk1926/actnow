package com.sdk.actnow.domain.users;

import com.sdk.actnow.oauth.domain.users.Users;
import com.sdk.actnow.oauth.domain.users.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        properties = {"spring.config.location=classpath:application-test.properties"}
)
public class UserRepositoryTest {

    @Autowired
    UsersRepository usersRepository;

    @AfterEach
    public void cleanup() {
        usersRepository.deleteAllInBatch();
    }

    @DisplayName("유저저장_불러오기")
    @Test
    public void saveUserRead() {
        //given
        String email = "sdk@naver.com";
        int snsId = 1234567;

        usersRepository.save(Users.builder()
                .email(email)
                .snsId(snsId)
                .build());

        //when
        List<Users> usersList = usersRepository.findAll();

        //then
        Users users = usersList.get(0);
        assertThat(users.getEmail()).isEqualTo(email);
        assertThat(users.getSnsId()).isEqualTo(snsId);
    }

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.of(2021, 10, 31, 0,0,0);
        usersRepository.save(Users.builder()
                .email("email")
                .snsId(123456)
                .build());

        //when
        List<Users> usersList = usersRepository.findAll();

        //then
        Users users = usersList.get(0);
        System.out.println(users.getCreatedDate());
        assertThat(users.getCreatedDate()).isAfter(now);
        assertThat(users.getModifiedDate().isAfter(now));
    }
}
