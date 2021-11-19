package com.sdk.actnow.announcement.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.announcement.domain.Announcement;
import com.sdk.actnow.announcement.domain.AnnouncementRepository;
import com.sdk.actnow.announcement.dto.AnnouncementRequestDto;
import com.sdk.actnow.announcement.dto.AnnouncementResponseDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.oauth.domain.users.Users;
import com.sdk.actnow.oauth.domain.users.UsersRepository;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UsersRepository usersRepository;
    private final Jwt jwt = new Jwt();

    @Transactional
    public ResponseEntity<Message> save(AnnouncementRequestDto announcementRequestDto, HttpServletRequest request) {
        try{
            if (!checkToken(request)){ return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            Users user = getUser(getSnsId(request));
            Announcement announcement = announcementRequestDto.toEntity(user);
            Announcement savedAnnouncement =  announcementRepository.save(announcement);
            return new ResponseEntity<>(new Message("SUCCESS",savedAnnouncement.getId()),HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("USER_NOT_EXIST"), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity findById(Long id, HttpServletRequest request) {
        try {
            Announcement announcement = findAnnouncementById(id);
            AnnouncementResponseDto announcementResponseDto = new AnnouncementResponseDto(announcement);
            return new ResponseEntity(announcementResponseDto,HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return jwt.checkClaim(token);
    }

    private Long getSnsId(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return jwt.getJwtContents(token).get("id",Long.class);
    }

    private Users getUser(Long snsId) {
        Users user = usersRepository.getBySnsId(snsId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return user;
    }

    private Announcement findAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("ANNOUNCEMENT_NOT_EXIST"));
        return announcement;
    }
}
