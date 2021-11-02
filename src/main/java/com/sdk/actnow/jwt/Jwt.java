package com.sdk.actnow.jwt;

import io.jsonwebtoken.*;
import java.util.Calendar;
import java.util.Date;

public class Jwt {

    private static final String secretKey = "secretKey";

    public String makeJwtToken(int id) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,7);

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
            System.out.println("Token Expired");  // 로그 배우고 리팩토링 하기
            return false;
        } catch (JwtException e) {
            System.out.println("Token Error");
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
