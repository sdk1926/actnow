package com.sdk.actnow.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

@WebAppConfiguration
@SpringBootTest
public class JwtTest {

    Jwt jwt = new Jwt();

    @Test
    public void jwt_생성() {
        // given
        String token = jwt.makeJwtToken(8);
        System.out.println(token);

        // when
        boolean checkClaim = jwt.checkClaim(token);
        Claims claims = jwt.getJwtContents(token);

        // then
        assertThat(checkClaim).isEqualTo(true);
        assertThat(claims.get("id")).isEqualTo(8);
    }
}
