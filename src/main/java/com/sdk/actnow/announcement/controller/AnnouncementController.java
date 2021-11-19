package com.sdk.actnow.announcement.controller;

import com.sdk.actnow.announcement.dto.AnnouncementRequestDto;
import com.sdk.actnow.announcement.dto.AnnouncementResponseDto;
import com.sdk.actnow.announcement.service.AnnouncementService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("/api/v1/announcement")
    public ResponseEntity<Message> save(@RequestBody AnnouncementRequestDto announcementRequestDto, HttpServletRequest requset) {
        return announcementService.save(announcementRequestDto,requset);
    }

    @GetMapping("/api/v1/announcement/{announcementId}")
    public ResponseEntity findById(@PathVariable(value = "announcementId")Long announcementId,
                                                            HttpServletRequest request) {
        return announcementService.findById(announcementId,request);
    }
}
