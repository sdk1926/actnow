package com.sdk.actnow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.domain.users.Users;
import com.sdk.actnow.domain.users.UsersRepository;
import com.sdk.actnow.dto.JwtResponseDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.oauth.KakaoProfile;
import com.sdk.actnow.oauth.OauthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sdk.actnow.oauth.KakaoConnection.*;

@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final Jwt jwt = new Jwt();

    @Transactional
    public JwtResponseDto save(String code){
        OauthToken accessToken = requestAuthCode(generateAuthCodeRequest(code)).getBody();
        String response = requestProfile(generateProfileRequest(accessToken)).getBody();
        KakaoProfile profile = null;
        try {
            profile = objectMapper.readValue(response, KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        int id = profile.getId();
        if (usersRepository.findBySnsId(id) == null) {
            Users user = Users.builder()
                    .snsId(id)
                    .email(profile.getKakao_account().getEmail())
                    .build();
            usersRepository.save(user);
        }
        String token = jwt.makeJwtToken(id);
        JwtResponseDto jwtResponseDto = new JwtResponseDto();
        jwtResponseDto.setMessage("success");
        jwtResponseDto.setToken(token);
        return jwtResponseDto;
    }

}
