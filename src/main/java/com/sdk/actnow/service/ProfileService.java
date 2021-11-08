package com.sdk.actnow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.domain.users.UsersRepository;
import com.sdk.actnow.dto.MessageDto;
import com.sdk.actnow.dto.ProfileRequestDto;
import com.sdk.actnow.jwt.Jwt;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final Jwt jwt = new Jwt();

    public MessageDto save(ProfileRequestDto profileRequestDto, String token){
        if (!jwt.checkClaim(token)){
            return new MessageDto("Wrong_token");
        }
        Claims claims = jwt.getJwtContents(token);
        var id = claims.get("id");

        return new MessageDto("success");
    };

}
