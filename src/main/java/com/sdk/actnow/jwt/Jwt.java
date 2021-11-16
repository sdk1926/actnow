package com.sdk.actnow.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class Jwt {

    private String secretKey = "fewseighiuwrgh";

    public String makeJwtToken(long id) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setIssuer("fresh")
                .setIssuedAt(now)
                .setExpiration(new Date(cal.getTimeInMillis()))
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())  // 시크릿 키 보안 추가 하기
                .compact();
    }

    public boolean checkClaim(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(jwt).getBody();
            return true;
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Claims getJwtContents(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

}
