package com.sdk.actnow.oauth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class KakaoConnection {

    private static final String CLIENT_ID = KakakoConfig.CLIENT_ID;

    private static final String CLIENT_SECRET = KakakoConfig.CLIENT_SECRET;

    private static final String REDIRECT_URL = KakakoConfig.REDIRECT_URL;

    public static MultiValueMap<String, String> generateParam(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URL);
        params.add("code", code);
        params.add("client_secret", CLIENT_SECRET);
        return params;
    }

    public static HttpEntity<MultiValueMap<String, String>> generateProfileRequest(OauthToken oauthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        return new HttpEntity<>(headers);
    }

    public static HttpEntity<MultiValueMap<String,String>> generateAuthCodeRequest(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return  new HttpEntity<>(generateParam(code), headers);
    }

    public static ResponseEntity<String> requestProfile(HttpEntity request) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                request,
                String.class
        );
    }

    public static ResponseEntity<OauthToken> requestAuthCode(HttpEntity request) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                OauthToken.class
        );
    }

    
}
