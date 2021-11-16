package com.sdk.actnow.profile.service;

import com.sdk.actnow.oauth.domain.users.Users;
import com.sdk.actnow.oauth.domain.users.UsersRepository;
import com.sdk.actnow.profile.dto.ProfileListResponseDto;
import com.sdk.actnow.profile.dto.ProfileRequestDto;
import com.sdk.actnow.jwt.Jwt;
import com.sdk.actnow.profile.domain.*;
import com.sdk.actnow.profile.dto.ProfileResponseDto;
import com.sdk.actnow.s3.S3Uploader;
import com.sdk.actnow.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    public ResponseEntity<Message> save(ProfileRequestDto profileRequestDto, HttpServletRequest request){
        try {
            if (!checkToken(request)){ return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            Users user = getUser(getSnsId(request));
            if (checkProfileUser(user)){
                return new ResponseEntity<>(new Message("PROFILE_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new Message("SUCCESS",saveProfile(profileRequestDto,user)), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("USER_NOT_EXIST"), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Message> saveImage(long profileId, MultipartFile multipartFile,
                                             HttpServletRequest request) throws IOException{
        try {
            if (!checkToken(request)) {
                return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST);
            }
            Profile profile = findProfile(profileId);
            Users user = getUser(getSnsId(request));
            if (!profile.getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(new Message("WRONG_ACCESS"), HttpStatus.BAD_REQUEST);
            }
            if (profile.getProfileImage() == null) {
                String url = s3Uploader.upload(multipartFile, "profile");
                ProfileImage profileImage = profileImageRepository.save(new ProfileImage(profile, url));
                return new ResponseEntity<>(new Message("SUCCESS", profileImage.getId()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Message("IMAGE_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("PROFILE_DOES_NOT_EXISTS"),HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Message> saveImages(Long profileId, List<MultipartFile> multipartFiles, HttpServletRequest request) {
        try {
            if (!checkToken(request)) { return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            List<String> urls = getUrls(multipartFiles);
            Profile profile = findProfile(profileId);
            List<ProfileImages> profileimages = getProfileImagesList(profile, urls);
            profileImagesRepository.saveAll(profileimages);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("PROFILE_DOES_NOT_EXISTS"),HttpStatus.BAD_REQUEST);
        } catch (IOException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("IO_ERROR"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Message("SUCCESS"), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ProfileResponseDto> findById(long id) {
        try {
            Profile profile = findProfile(id);
            List<Specialty> specialties = specialtyRepository.findAllByProfileId(id);
            List<Career> careers = careerRepository.findAllByProfileId(id);
            ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                    .profile(profile)
                    .specialties(specialties)
                    .careers(careers)
                    .build();
            return new ResponseEntity<>(profileResponseDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ProfileResponseDto(),HttpStatus.BAD_REQUEST);
        }
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

    @Transactional
    public ResponseEntity<Message> update(Long profileId, ProfileRequestDto profileRequestDto,HttpServletRequest request){
        try {
            if (!checkToken(request)) { return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            long snsId = getSnsId(request);
            Profile profile = findProfile(profileId);
            if(profile.getUser().getSnsId() != snsId){ return new ResponseEntity<>(new Message("WRONG_ACCESS"), HttpStatus.BAD_REQUEST); }
            profile.update(profileRequestDto);
            return new ResponseEntity<>(new Message("SUCCESS"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("PROFILE_DOES_NOT_EXISTS"),HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Message> updateImage(Long profileImageId, MultipartFile multipartFile, HttpServletRequest request) throws IOException{
        try {
            if (!checkToken(request)) {
                return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST);
            }
            ProfileImage profileImage = profileImageRepository.findById(profileImageId).orElseThrow(()-> new IllegalArgumentException("이미지 없음"));
            Users user = getUser(getSnsId(request));
            if (!profileImage.getProfile().getUser().equals(user)) {
                return new ResponseEntity<>(new Message("WRONG_ACCESS"), HttpStatus.BAD_REQUEST);
            }
            s3Uploader.deleteS3(profileImage.getProfileURL());
            String url = s3Uploader.upload(multipartFile, "profile");
            profileImage.update(url);
            return new ResponseEntity<>(new Message("SUCCESS",profileImage.getId()), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("PROFILE_DOES_NOT_EXISTS"),HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Message> deleteImages(long profileImageId,HttpServletRequest request) {
        try {
            if (!checkToken(request)) { return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            long snsId = getSnsId(request);
            Users user = getUser(snsId);
            ProfileImages profileImages = getProfileImages(profileImageId);
            if (profileImages.getProfile().getUser() != user) {
                return new ResponseEntity<>(new Message("NOT_HAVE_PERMISSION_TO_DELETE"), HttpStatus.BAD_REQUEST);
            }
            String fileName = profileImages.getProfileURL();
            s3Uploader.deleteS3(fileName);
            profileImagesRepository.deleteById(profileImageId);
            return new ResponseEntity<>(new Message("SUCCESS"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("IMAGES_DOES_NOT_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return jwt.checkClaim(token);
    }

    private boolean checkProfileUser(Users user){
        if (profileRepository.findByUser(user) != null) return true;
        return false;
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

    private Profile findProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 없습니다. id="+profileId));
        return profile;
    }

    private Long saveProfile(ProfileRequestDto profileRequestDto, Users user) {
        Profile profile = profileRepository.save(profileRequestDto.toEntity(user));
        specialtyRepository.saveAll(profileRequestDto.toEntity(profile));
        careerRepository.saveAll(profileRequestDto.toEntityCareer(profile));
        return profile.getId();
    }

    private List<String> getUrls(List<MultipartFile> multipartFiles) throws IOException{
        List<String> urls = new ArrayList<>();
        for(MultipartFile file: multipartFiles) {
            urls.add(s3Uploader.upload(file,"profile"));
        }
        return urls;
    }

    private List<ProfileImages> getProfileImagesList (Profile profile, List<String> urls){
        List<ProfileImages> profileimages = new ArrayList<>();
        for(String url:urls){
            ProfileImages profileImages = new ProfileImages(profile,url);
            profileimages.add(profileImages);
        }
        return profileimages;
    }

    private ProfileImages getProfileImages(Long profileImagesId){
        ProfileImages profileImages = profileImagesRepository.findById(profileImagesId)
                .orElseThrow(() -> new IllegalArgumentException("IMAGES_DOES_NOT_EXISTS"));
        return profileImages;
    }

}
