package com.sdk.actnow.controller;

import com.sdk.actnow.dto.MessageDto;
import com.sdk.actnow.dto.ProfileRequestDto;
import com.sdk.actnow.s3.ProfileModel;
import com.sdk.actnow.s3.S3Uploader;
import com.sdk.actnow.service.ProfileService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;
    private final S3Uploader s3Uploader;

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

    @PostMapping("api/v1/profile/images")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

    @PostMapping("api/v1/profile/imagess")
    public ResponseEntity<Message> upload2(@RequestPart(value = "data", required = false) List<MultipartFile> multipartFiles) throws IOException {
        for(MultipartFile f:multipartFiles){
            System.out.println(f.getOriginalFilename());
        }
        return profileService.profilesSave(multipartFiles);
    }
}
