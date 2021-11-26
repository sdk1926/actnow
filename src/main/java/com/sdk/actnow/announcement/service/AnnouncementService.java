package com.sdk.actnow.announcement.service;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final Jwt jwt;

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
    public ResponseEntity findById(Long id) {
        try {
            Announcement announcement = findAnnouncementById(id);
            AnnouncementResponseDto announcementResponseDto = new AnnouncementResponseDto(announcement);
            return new ResponseEntity(announcementResponseDto,HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity findAll(Pageable pageable) {
        Page<Announcement> announcements = announcementRepository.findAll(pageable);
        Page<AnnouncementResponseDto> announcementResponseDtos = announcements.map(
                announcement -> AnnouncementResponseDto.builder()
                        .id(announcement.getId())
                        .title(announcement.getTitle())
                        .producer(announcement.getProducer())
                        .name(announcement.getName())
                        .kind(announcement.getKind())
                        .directorName(announcement.getDirectorName())
                        .role(announcement.getRole())
                        .age(announcement.getAge())
                        .shootingPeriod(announcement.getShootingPeriod())
                        .pay(announcement.getPay())
                        .manager(announcement.getMaanger())
                        .email(announcement.getEmail())
                        .gender(announcement.getGender())
                        .deadline(announcement.getDeadline())
                        .details(announcement.getDetails())
                        .build());
        return new ResponseEntity(announcementResponseDtos,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> update(Long announcementId, AnnouncementRequestDto announcementRequestDto,HttpServletRequest request) {
        try{
            if (!checkToken(request)){ return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            Users user = getUser(getSnsId(request));
            Announcement announcement = findAnnouncementById(announcementId);
            if (!announcement.getUser().equals(user)){return new ResponseEntity<>(new Message("WRONG_ACCESS"),HttpStatus.BAD_REQUEST);}
            System.out.println(announcementRequestDto.getDeadline());
            announcement.update(announcementRequestDto);
            return new ResponseEntity<>(new Message("SUCCESS",announcement.getId()),HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("WRONG_ACCESS"), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Message> delete(Long announcementId, HttpServletRequest request) {
        try{
            if (!checkToken(request)){ return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            Users user = getUser(getSnsId(request));
            Announcement announcement = findAnnouncementById(announcementId);
            if (!announcement.getUser().equals(user)){return new ResponseEntity<>(new Message("WRONG_ACCESS"),HttpStatus.BAD_REQUEST);}
            announcementRepository.deleteById(announcementId);
            return new ResponseEntity<>(new Message("SUCCESS",announcement.getId()),HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("WRONG_ACCESS"), HttpStatus.BAD_REQUEST);
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

    private Announcement findAnnouncementByUser(Users user) {
        Announcement announcement = announcementRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공고입니다."));
        return announcement;
    }

    private Announcement findAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("ANNOUNCEMENT_NOT_EXIST"));
        return announcement;
    }
}
