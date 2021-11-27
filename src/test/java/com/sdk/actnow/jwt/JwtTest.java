package com.sdk.actnow.jwt;

import com.sdk.actnow.profile.domain.ProfileImagesRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@WebAppConfiguration
@SpringBootTest(
        properties = {"spring.config.location=classpath:application-test.properties"}
)
public class JwtTest {

    Jwt jwt;


    @Test
    public void jwt_생성() {
        // given
        String token = jwt.makeJwtToken(8);
        System.out.println(token);
        LocalDate date = LocalDate.of(2018,3,3);
        System.out.println(date);

        // when
        boolean checkClaim = jwt.checkClaim(token);
        Claims claims = jwt.getJwtContents(token);

        // then
        assertThat(checkClaim).isEqualTo(true);
        assertThat(claims.get("id")).isEqualTo(8);
    }
}
