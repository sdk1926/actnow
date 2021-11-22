package com.sdk.actnow.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.oauth.domain.users.Users;
import com.sdk.actnow.oauth.domain.users.UsersRepository;
import com.sdk.actnow.oauth.response.JwtResponse;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.oauth.KakaoProfile;
import com.sdk.actnow.oauth.OauthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    @Value( "${kakao.client_id}" )
    private String CLIENT_ID;

    @Value("${kakao.client_secret}")
    private String CLIENT_SECRET;

    @Value("${kakao.redirect_uri}")
    private String REDIRECT_URL;

    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final Jwt jwt;

    @Transactional
    public ResponseEntity<JwtResponse> save(String code){
        try {
            OauthToken accessToken = requestAuthCode(generateAuthCodeRequest(code)).getBody();
            String userProfileResponse = requestProfile(generateProfileRequest(accessToken)).getBody();
            KakaoProfile kakaoProfile = responseToKakaoProfile(userProfileResponse);
            long snsId = kakaoProfile.getId();
            saveUser(snsId,kakaoProfile);
            String token = jwt.makeJwtToken(snsId);
            return new ResponseEntity<>(new JwtResponse("SUCCESS",token), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new JwtResponse("JsonParcingError"),HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new JwtResponse("Wrong_token"),HttpStatus.BAD_REQUEST);
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
