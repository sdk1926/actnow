package com.sdk.actnow.controller;

import com.sdk.actnow.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/api/v1/log-in")
    public String save(@RequestParam String code){return kakaoLoginService.save(code);}
}
