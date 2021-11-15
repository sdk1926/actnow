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
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
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
            if (profileRepository.findByUser(user) != null){
                return new ResponseEntity<>(new Message("Profile_already_exist"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new Message("SUCCESS",saveProfile(profileRequestDto,user)), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("USER_NOT_EXIST"), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Message> saveImage(long profileId, MultipartFile multipartFile, HttpServletRequest request) throws IOException{
        try {
            if (!checkToken(request)) { return new ResponseEntity<>(new Message("WRONG_TOKEN"), HttpStatus.BAD_REQUEST); }
            Profile profile = findProfile(profileId);
            Users user = getUser(getSnsId(request));
            if (profile.getUser().getId() != user.getId()) {return new ResponseEntity<>(new Message("WRONG_ACCESS"), HttpStatus.BAD_REQUEST);}
            if (profile.getProfileImage() == null) {
                String url = s3Uploader.upload(multipartFile, "profile");
                ProfileImage profileImage = profileImageRepository.save(new ProfileImage(profile, url));
                return new ResponseEntity<>(new Message("SUCCESS", profileImage.getId()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Message("Image_already_exists"), HttpStatus.BAD_REQUEST);
        }catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new Message("Profile_Does_Not_Exists"),HttpStatus.BAD_REQUEST);
        }
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

    @Transactional
    public ResponseEntity<Message> update(Long profileId, ProfileRequestDto profileRequestDto,String token){
        Message message = new Message();

        if (!jwt.checkClaim(token)){
            message.setMessage("WRONG_TOKEN");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        int id = jwt.getJwtContents(token).get("id",Integer.class);

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 없습니다. id="+profileId));

        if(profile.getUser().getSnsId() != id){
            message.setMessage("WRONG_ACCESS");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        profile.update(profileRequestDto);
        message.setMessage("SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Message> deleteImages(long profileImageId,String token) throws IOException{
        Message message = new Message();

        if (!jwt.checkClaim(token)){
            message.setMessage("WRONG_TOKEN");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        Long id = jwt.getJwtContents(token).get("id",Long.class);
        Users user = getUser(id);
        Long profileId = profileRepository.findByUserId(user.getId()).getId();

        if (profileImagesRepository.getById(profileImageId).getProfile().getId() != profileId){
            message.setMessage("삭제 권한이 없습니다.");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        profileImagesRepository.deleteById(profileImageId);
        message.setMessage("SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);
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
        Users users = usersRepository.getBySnsId(snsId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return users;
    }

    private Profile findProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 없습니다. id="));
        return profile;
    }

    private Long saveProfile(ProfileRequestDto profileRequestDto, Users user) {
        Profile profile = profileRepository.save(profileRequestDto.toEntity(user));
        specialtyRepository.saveAll(profileRequestDto.toEntity(profile));
        careerRepository.saveAll(profileRequestDto.toEntityCareer(profile));
        return profile.getId();
    }

//    private boolean isEqualProfileUserIdUserId(Users user, Profile profile){
//        if (profile.getUser().getId() == user.getId()) {
//            return true;
//        }
//        return false;
//    }

}
