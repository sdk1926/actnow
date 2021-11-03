package com.sdk.actnow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.domain.users.Users;
import com.sdk.actnow.domain.users.UsersRepository;
import com.sdk.actnow.dto.JwtDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.oauth.KakaoConnection;
import com.sdk.actnow.oauth.KakaoProfile;
import com.sdk.actnow.oauth.OauthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private KakaoConnection kakaoConnection;
    private Jwt jwt;

    @Transactional
    public JwtDto save(String code){
        OauthToken accessToken = kakaoConnection.requestAuthCode(kakaoConnection.generateAuthCodeRequest(code)).getBody();
        String response = kakaoConnection.requestProfile(kakaoConnection.generateProfileRequest(accessToken)).getBody();
        KakaoProfile profile = null;
        try {
            profile = objectMapper.readValue(response, KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        int id = profile.getId();
        Users user = Users.builder()
                .snsId(id)
                .email(profile.getKakao_account().getEmail())
                .build();
        usersRepository.save(user);
        String token = jwt.makeJwtToken(user.getId());
        JwtDto jwtDto = new JwtDto();
        jwtDto.setMessage("success");
        jwtDto.setToken(token);
        return jwtDto;
    }
}
