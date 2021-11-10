package com.sdk.actnow.controller;

import com.sdk.actnow.dto.MessageDto;
import com.sdk.actnow.dto.ProfileRequestDto;
import com.sdk.actnow.s3.ProfileModel;
import com.sdk.actnow.service.ProfileService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/api/v1/profile")
    public ResponseEntity<Message> save(@RequestBody ProfileRequestDto profileRequestDto, HttpServletRequest requset) {
        String token = requset.getHeader("Authorization");
        return profileService.save(profileRequestDto,token);
    }

    @PostMapping("api/v1/profile/image")
    public ResponseEntity<Message> saveProfile(ProfileModel profileModel,
                                      @RequestPart(value = "profile", required = false)
                                      final MultipartFile multipartFile) {
        return profileService.profileSave(profileModel,multipartFile);
    }
}
