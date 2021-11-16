package com.sdk.actnow.profile.controller;

import com.sdk.actnow.profile.dto.ProfileRequestDto;
import com.sdk.actnow.profile.dto.ProfileResponseDto;
import com.sdk.actnow.profile.service.ProfileService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/api/v1/profile/{profileId}")
    public ResponseEntity<ProfileResponseDto> findById(@PathVariable(value = "profileId") long profileId){
        return profileService.findById(profileId);
    }

    @GetMapping("api/v1/profiles")
    public ResponseEntity findAll(@PageableDefault (size=10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return profileService.findAll(pageable);
    }

    @PostMapping("/api/v1/profile")
    public ResponseEntity<Message> save(@RequestBody ProfileRequestDto profileRequestDto, HttpServletRequest requset) {
        return profileService.save(profileRequestDto,requset);
    }

    @PostMapping("api/v1/profile/image/{profileId}")
    public ResponseEntity<Message> saveImage(@PathVariable(value = "profileId")long profileId,
                                          @RequestPart(value = "profile") MultipartFile multipartFile,
                                          HttpServletRequest request) throws IOException {
        System.out.println("이미지 도착");
        return profileService.saveImage(profileId, multipartFile, request);
    }

    @PostMapping("api/v1/profile/images/{profileId}")
    public ResponseEntity<Message> saveImages(@PathVariable(value = "profileId")long profileId,
                                              @RequestPart(value = "profiles")List<MultipartFile> multipartFiles,
                                              HttpServletRequest request) throws IOException {
        System.out.println("이미지 도착");
        return profileService.saveImages(profileId, multipartFiles,request);
    }

    @PutMapping("/api/v1/profile/{profileId}")
    public ResponseEntity<Message> update(@PathVariable(value = "profileId")Long profileId,
                                          @RequestBody ProfileRequestDto profileRequestDto,
                                          HttpServletRequest request){
        return profileService.update(profileId,profileRequestDto,request);
    }

    @PutMapping("/api/v1/profile/image/{profileId}")
    public ResponseEntity<Message> updateImage(@PathVariable(value = "profileId")Long profileId,
                                               @RequestPart(value = "profile") MultipartFile multipartFile,
                                               HttpServletRequest request) throws IOException {
        return profileService.updateImage(profileId, multipartFile, request);
    }


    @DeleteMapping("api/v1/profile/images/{profileImageId}")
    public ResponseEntity<Message> delete(@PathVariable(value = "profileImageId")Long profileImageId,
                                          HttpServletRequest request) throws IOException{
        return profileService.deleteImages(profileImageId,request);
    }

}
