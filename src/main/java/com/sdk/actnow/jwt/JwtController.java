package com.sdk.actnow.jwt;

import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
@RestController
public class JwtController {

    Jwt jwt;

    @GetMapping("api/v1/token")
    public ResponseEntity checkToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (jwt.checkClaim(token) ) {
            return new ResponseEntity(new Message("SUCCESS"), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("WRONG_TOKEN"),HttpStatus.BAD_REQUEST);
    }
}
