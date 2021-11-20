package com.sdk.actnow.announcement.controller;

import com.sdk.actnow.announcement.dto.AnnouncementRequestDto;
import com.sdk.actnow.announcement.dto.AnnouncementResponseDto;
import com.sdk.actnow.announcement.service.AnnouncementService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
    public ResponseEntity findById(@PathVariable(value = "announcementId")Long announcementId) {
        return announcementService.findById(announcementId);
    }

    @GetMapping("api/v1/announcement")
    public ResponseEntity findAll(@PageableDefault (size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        return announcementService.findAll(pageable);
    }

    @PutMapping("/api/v1/announcement/{announcementId}")
    public ResponseEntity<Message> update(@PathVariable(value = "announcementId")Long announcementId,
                                 @RequestBody AnnouncementRequestDto announcementRequestDto,
                                 HttpServletRequest request) {
        return announcementService.update(announcementId,announcementRequestDto,request);
    }

    @DeleteMapping("api/v1/announcement/{announcementId}")
    public ResponseEntity<Message> delete(@PathVariable(value = "announcementId")Long announcementId,
                                          HttpServletRequest request) {
        return announcementService.delete(announcementId,request);
    }

}
