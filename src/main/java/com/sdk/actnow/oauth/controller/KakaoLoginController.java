package com.sdk.actnow.oauth.controller;

import com.sdk.actnow.oauth.response.JwtResponse;
import com.sdk.actnow.oauth.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/api/v1/log-in")
    public ResponseEntity<JwtResponse> save(@RequestParam String code){
        System.out.println("Called");
        return kakaoLoginService.save(code);}

}
