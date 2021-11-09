package com.sdk.actnow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.domain.profile.Profile;
import com.sdk.actnow.domain.profile.ProfileRepository;
import com.sdk.actnow.domain.profile.SpecialtyRepository;
import com.sdk.actnow.domain.users.Users;
import com.sdk.actnow.domain.users.UsersRepository;
import com.sdk.actnow.dto.ProfileRequestDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.util.Message;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final ProfileRepository profileRepository;
    private final SpecialtyRepository specialtyRepository;
    private final Jwt jwt = new Jwt();

    @Transactional
    public ResponseEntity<Message> save(ProfileRequestDto profileRequestDto, String token){
        Message message = new Message();

        if (!jwt.checkClaim(token)){
            message.setMessage("WRONG_TOKEN");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        Claims claims = jwt.getJwtContents(token);
        long id = claims.get("id",Long.class);

        Users user = usersRepository.getById(id);
        Profile profile = profileRepository.save(profileRequestDto.toEntity(user));
        specialtyRepository.saveAll(profileRequestDto.toEntity(profile));


        message.setMessage("SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
