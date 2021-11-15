package com.sdk.actnow.controller;

import com.sdk.actnow.profile.domain.ProfileRepository;
import com.sdk.actnow.profile.dto.ProfileRequestDto;
import com.sdk.actnow.util.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void profile등록(){
        List<String> specialty = new ArrayList<>();
        specialty.add("노래");
        specialty.add("춤");
        List<ProfileRequestDto.CareerDto> careers = new ArrayList<>();
        ProfileRequestDto.CareerDto career = new ProfileRequestDto.CareerDto(2017,"풀땅","주연","단편");
        careers.add(career);
        String url = "http://localhost:"+"8080"+"/api/v1/profile";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmcmVzaCIsImlhdCI6MTYzNjY4NTUyNiwiZXhwIjoxNjM3MjkwMzI2LCJpZCI6OH0.arRhXxTrfxLx0wkP1gNYacvCIMZLWfkua0_zIm_2RjQ";
        ProfileRequestDto profileRequestDto = ProfileRequestDto.builder()
                .age(15)
                .height(171)
                .weight(65)
                .email("sdk@naver.com")
                .phoneNumber("01085989376")
                .snsAddress("wfbue@efev")
                .aboutMe("하이요~")
                .specialty(specialty)
                .career(careers)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);
        HttpEntity<ProfileRequestDto> request = new HttpEntity(profileRequestDto,headers);
        ResponseEntity<Message> responseEntity = restTemplate.postForEntity(url, request,Message.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
