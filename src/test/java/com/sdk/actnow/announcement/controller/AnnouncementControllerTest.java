package com.sdk.actnow.announcement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.announcement.dto.AnnouncementRequestDto;
import com.sdk.actnow.announcement.service.AnnouncementService;
import com.sdk.actnow.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AnnouncementControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AnnouncementService announcementService;

    @Test
    @DisplayName("공고_저장_컨트롤러_테스트")
    void saveAnnouncementControllerTest() throws Exception{
        //given
        ResponseEntity<Message> response = new ResponseEntity<>(new Message("Success",3), HttpStatus.OK);
        java.time.LocalDate date = LocalDate.of(2018,3,3);
        AnnouncementRequestDto announcementRequestDto = AnnouncementRequestDto.builder()
                .name("name")
                .kind("kind")
                .directorName("sohyeon")
                .role("role")
                .age("age")
                .shootingPeriod("shootingPeriod")
                .pay("pay")
                .manager("manager")
                .email("email")
                .deadline(date)
                .details("defeg")
                .build();
        given(announcementService.save(any(AnnouncementRequestDto.class),any(HttpServletRequest.class))).willReturn(response);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        //when
        mvc.perform(post("/api/v1/announcement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(announcementRequestDto))
                .header("Authorization","fakeToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
        verify(announcementService).save(any(AnnouncementRequestDto.class),any(HttpServletRequest.class));

    }


}
