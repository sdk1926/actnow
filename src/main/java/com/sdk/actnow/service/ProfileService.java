package com.sdk.actnow.service;

import com.sdk.actnow.domain.profile.*;
import com.sdk.actnow.domain.users.Users;
import com.sdk.actnow.domain.users.UsersRepository;
import com.sdk.actnow.dto.ProfileRequestDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.s3.ProfileModel;
import com.sdk.actnow.s3.S3FileUploadService;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UsersRepository usersRepository;
    private final ProfileRepository profileRepository;
    private final SpecialtyRepository specialtyRepository;
    private final CareerRepository careerRepository;
    private final ProfileImageRepository profileImageRepository;
    private final Jwt jwt = new Jwt();

    @Autowired
    private final S3FileUploadService s3FileUploadService;

    @Transactional
    public ResponseEntity<Message> save(ProfileRequestDto profileRequestDto, String token){
        Message message = new Message();

        if (!jwt.checkClaim(token)){
            message.setMessage("WRONG_TOKEN");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        Long id = jwt.getJwtContents(token).get("id",Long.class);
        Users user = usersRepository.getById(id);
        Profile profile = profileRepository.save(profileRequestDto.toEntity(user));
        specialtyRepository.saveAll(profileRequestDto.toEntity(profile));
        careerRepository.saveAll(profileRequestDto.toEntityCareer(profile));

        message.setMessage("SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> profileSave(ProfileModel profileModel, MultipartFile multipartFile){
        Message message = new Message();
        try {
            if (multipartFile != null) {
                profileModel.setProfileURL(s3FileUploadService.upload(multipartFile));
                ProfileImage profileImage = ProfileImage.builder()
                        .profileName(profileModel.getProfileName())
                        .profileURL(profileModel.getProfileURL())
                        .build();

                profileImageRepository.save(profileImage);
                message.setMessage("SUCCESS");
            }
            return new ResponseEntity<>(message,HttpStatus.OK);
        } catch (Exception e) {
            message.setMessage("FAIL");
            return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
