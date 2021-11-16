package com.sdk.actnow.jwt;

import com.sdk.actnow.profile.domain.ProfileImagesRepository;
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
        String s = "https://actnow-bucket.s3.ap-northeast-2.amazonaws.com/profile/0c0cf4e947ac43dcb3327546f3c0e8d7서핑1.jpeg.jpg";
        System.out.println(s.length());
        System.out.println(s.substring(53,s.length()));

        // when
        boolean checkClaim = jwt.checkClaim(token);
        Claims claims = jwt.getJwtContents(token);

        // then
        assertThat(checkClaim).isEqualTo(true);
        assertThat(claims.get("id")).isEqualTo(8);
    }
}
