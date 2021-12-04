package com.sdk.actnow.announcement.controller;

import com.sdk.actnow.announcement.dto.AnnouncementRequestDto;
import com.sdk.actnow.announcement.service.AnnouncementService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Message> hadleValidationExceptions(
            MethodArgumentNotValidException exception
    ) {
        Message message = new Message();
        exception.getBindingResult().getAllErrors().forEach((error) ->{
            String errorMessage = error.getDefaultMessage();
            message.setMessage(errorMessage);

        });
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/v1/announcement")
    public ResponseEntity<Message> save(@RequestBody @Valid AnnouncementRequestDto announcementRequestDto, HttpServletRequest requset) {
        return announcementService.save(announcementRequestDto,requset);
    }

    @GetMapping("/api/v1/announcement/{announcementId}")
    public ResponseEntity findById(@PathVariable(value = "announcementId")Long announcementId,
                                   HttpServletRequest request) {
        return announcementService.findById(announcementId, request);
    }

    @GetMapping("/api/v1/announcement")
    public ResponseEntity findAll(@PageableDefault (size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        return announcementService.findAll(pageable);
    }

    @GetMapping("/api/v1/user/announcement")
    public ResponseEntity findAllByUser(@PageableDefault (size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
                                        HttpServletRequest request){
        return announcementService.findAllByUser(pageable, request);
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
