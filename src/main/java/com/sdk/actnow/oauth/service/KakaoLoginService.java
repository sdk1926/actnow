package com.sdk.actnow.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.oauth.KakaoConfig;
import com.sdk.actnow.oauth.domain.users.Users;
import com.sdk.actnow.oauth.domain.users.UsersRepository;
import com.sdk.actnow.oauth.dto.JwtResponseDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.oauth.KakaoProfile;
import com.sdk.actnow.oauth.OauthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private static final String CLIENT_ID = KakaoConfig.CLIENT_ID;
    private static final String CLIENT_SECRET = KakaoConfig.CLIENT_SECRET;
    private static final String REDIRECT_URL = KakaoConfig.REDIRECT_URL;
    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final Jwt jwt = new Jwt();

    @Transactional
    public JwtResponseDto save(String code){
        try {
            OauthToken accessToken = requestAuthCode(generateAuthCodeRequest(code)).getBody();
            String userProfileResponse = requestProfile(generateProfileRequest(accessToken)).getBody();
            KakaoProfile kakaoProfile = responseToKakaoProfile(userProfileResponse);
            long snsId = kakaoProfile.getId();
            saveUser(snsId,kakaoProfile);
            String token = jwt.makeJwtToken(snsId);
            return new JwtResponseDto("SUCCESS",token);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new JwtResponseDto("JsonParcingError");
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return new JwtResponseDto("Wrong_token");
        }
    }

    private KakaoProfile responseToKakaoProfile(String userProfileResponse) throws JsonProcessingException{
        KakaoProfile kakaoProfile = objectMapper.readValue(userProfileResponse, KakaoProfile.class);
        return kakaoProfile;
    }

    private void saveUser(long id, KakaoProfile profile){
        if (usersRepository.getBySnsId(id).isEmpty()) {
            Users user = Users.builder()
                    .snsId(id)
                    .email(profile.getKakao_account().getEmail())
                    .build();
            usersRepository.save(user);
        }
    }

    private MultiValueMap<String, String> generateParam(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URL);
        params.add("code", code);
        params.add("client_secret", CLIENT_SECRET);
        return params;
    }

    private HttpEntity<MultiValueMap<String, String>> generateProfileRequest(OauthToken oauthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        return new HttpEntity<>(headers);
    }

    private HttpEntity<MultiValueMap<String,String>> generateAuthCodeRequest(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return  new HttpEntity<>(generateParam(code), headers);
    }

    private ResponseEntity<String> requestProfile(HttpEntity request) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                request,
                String.class
        );
    }

    private ResponseEntity<OauthToken> requestAuthCode(HttpEntity request) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                OauthToken.class
        );
    }
}
