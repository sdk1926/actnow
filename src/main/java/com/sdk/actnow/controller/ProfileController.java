package com.sdk.actnow.controller;

import com.sdk.actnow.dto.MessageDto;
import com.sdk.actnow.dto.ProfileRequestDto;
import com.sdk.actnow.service.ProfileService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/api/v1/profile")
    public ResponseEntity<Message> save(@RequestBody ProfileRequestDto profileRequestDto, HttpServletRequest requset) {
        String token = requset.getHeader("Authorization");
        return profileService.save(profileRequestDto,token);
    }
}
