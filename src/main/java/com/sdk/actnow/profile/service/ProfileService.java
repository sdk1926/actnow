package com.sdk.actnow.profile.service;

import com.sdk.actnow.domain.users.Users;
import com.sdk.actnow.domain.users.UsersRepository;
import com.sdk.actnow.profile.dto.ProfileListResponseDto;
import com.sdk.actnow.profile.dto.ProfileRequestDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.profile.domain.*;
import com.sdk.actnow.profile.dto.ProfileResponseDto;
import com.sdk.actnow.s3.S3Uploader;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UsersRepository usersRepository;
    private final ProfileRepository profileRepository;
    private final SpecialtyRepository specialtyRepository;
    private final CareerRepository careerRepository;
    private final ProfileImageRepository profileImageRepository;
    private final ProfileImagesRepository profileImagesRepository;
    private final S3Uploader s3Uploader;
    private final Jwt jwt = new Jwt();

    @Transactional
    public ResponseEntity<Message> save(ProfileRequestDto profileRequestDto, String token, HttpServletRequest request){
        Message message = new Message();

        if (!jwt.checkClaim(token)){
            message.setMessage("WRONG_TOKEN");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        int id = jwt.getJwtContents(token).get("id",Integer.class);
        Users user= usersRepository.findBySnsId(id);
        Profile profile = profileRepository.save(profileRequestDto.toEntity(user));
        specialtyRepository.saveAll(profileRequestDto.toEntity(profile));
        careerRepository.saveAll(profileRequestDto.toEntityCareer(profile));

        message.setMessage("SUCCESS");
        message.setId(profile.getId());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> saveImage(long profileId, MultipartFile multipartFile, String token) throws IOException{
        Message message = new Message();

        if (!jwt.checkClaim(token)){
            message.setMessage("WRONG_TOKEN");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 없습니다. id="+profileId));
        if (profile.getProfileImage() == null){
            String url = s3Uploader.upload(multipartFile, "profile");
            ProfileImage profileImage = new ProfileImage(profile,url);
            profileImageRepository.save(profileImage);
            message.setMessage("SUCCESS");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        message.setMessage("Image aleady exists");
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }

    @Transactional
    public ResponseEntity<Message> saveImages(Long profileId, List<MultipartFile> multipartFiles, String token) throws IOException {
        Message message = new Message();

        if (!jwt.checkClaim(token)){
            message.setMessage("WRONG_TOKEN");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        List<String> urls = new ArrayList<>();
        for(MultipartFile file: multipartFiles) {
            urls.add(s3Uploader.upload(file,"profile"));
        }
        System.out.println("Success");
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 없습니다. id="+profileId));
        System.out.println("여기서 쿼리가 생기니?");
        List<ProfileImages> profileimages = new ArrayList<>();
        for(String url:urls){
            ProfileImages profileImages = new ProfileImages(profile,url);
            profileimages.add(profileImages);
        }

        profileImagesRepository.saveAll(profileimages);
        message.setMessage("SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ProfileResponseDto> findById(long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 없습니다. id="+id));
        List<Specialty> specialties = specialtyRepository.findAllByProfileId(id);
        List<Career> careers = careerRepository.findAllByProfileId(id);
        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .profile(profile)
                .specialties(specialties)
                .careers(careers)
                .build();
        return new ResponseEntity<>(profileResponseDto,HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity findAll(Pageable pageable) {
        Page<Profile> profiles = profileRepository.findAll(pageable);
        Page<ProfileListResponseDto> profileListResponseDtos = profiles.map(
                profile -> ProfileListResponseDto.builder()
                        .profile(profile)
                        .build());
        return new ResponseEntity(profileListResponseDtos,HttpStatus.OK);
    }

}
