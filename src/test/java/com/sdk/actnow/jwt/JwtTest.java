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
        String token = jwt.makeJwtToken(1);

        // when
        boolean checkClaim = jwt.checkClaim(token);
        Claims claims = jwt.getJwtContents(token);

        // result
        assertThat(checkClaim).isEqualTo(true);
        assertThat(claims.get("id")).isEqualTo(1);
    }
}
