package com.sdk.actnow.service;

import com.sdk.actnow.domain.users.UsersRepository;
import com.sdk.actnow.oauth.KakaoConnection;
import com.sdk.actnow.oauth.OauthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private final UsersRepository usersRepository;
    private KakaoConnection kakaoConnection;

    @Transactional
    public String save(String code){
        return "save";
    }
}
