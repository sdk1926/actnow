package com.sdk.actnow.announcement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.actnow.announcement.dto.AnnouncementRequestDto;
import com.sdk.actnow.announcement.dto.AnnouncementResponseDto;
import com.sdk.actnow.announcement.service.AnnouncementService;
import com.sdk.actnow.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        properties = {"spring.config.location=classpath:application-test.properties"}
)
@AutoConfigureMockMvc
public class AnnouncementControllerTest {

    static java.time.LocalDate date = LocalDate.of(2018,3,3);
    @Autowired
    MockMvc mvc;

    @MockBean
    AnnouncementService announcementService;

    @Test
    @DisplayName("공고_저장_컨트롤러_테스트")
    void saveAnnouncementControllerTest() throws Exception{
        //given
        ResponseEntity<Message> response = new ResponseEntity<>(new Message("Success",3), HttpStatus.OK);
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

    @Test
    @DisplayName("공고_단일_조회_컨트롤러_테스트")
    void findOneAnnouncementControllerTest() throws Exception {
        // given
        ResponseEntity response = new ResponseEntity(AnnouncementResponseDto.builder()
                .id(1L)
                .title("title")
                .producer("producer")
                .name("name")
                .kind("kind")
                .directorName("directorname")
                .role("role")
                .age("age")
                .shootingPeriod("shootinfperid")
                .pay("pay")
                .manager("manager")
                .email("email")
                .gender("gender")
                .deadline(date)
                .details("details")
                .build(), HttpStatus.OK);
        given(announcementService.findById(any(Long.class),any(HttpServletRequest.class))).willReturn(response);

        //when
        mvc.perform(get("/api/v1/announcement/1")
                .header("Authorization","fakeToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
        verify(announcementService).findById(any(Long.class),any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("공고_리스트_조회_컨트롤러_테스트")
    void findAllAnnouncementControllerTest() throws Exception {
        // given
        ResponseEntity<Page<AnnouncementResponseDto>> response = new ResponseEntity(AnnouncementResponseDto.builder()
                .id(1L)
                .title("title")
                .producer("producer")
                .name("name")
                .kind("kind")
                .directorName("directorname")
                .role("role")
                .age("age")
                .shootingPeriod("shootinfperid")
                .pay("pay")
                .manager("manager")
                .email("email")
                .gender("gender")
                .deadline(date)
                .details("details")
                .build(), HttpStatus.OK);
        given(announcementService.findAll(any(Pageable.class))).willReturn(response);

        //when
        mvc.perform(get("/api/v1/announcement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email"));
        verify(announcementService).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("특정_유저가_쓴_공고_리스트_조회_컨트롤러_테스트")
    void findAllByUserAnnouncementControllerTest() throws Exception {
        // given
        ResponseEntity<Page<AnnouncementResponseDto>> response = new ResponseEntity(AnnouncementResponseDto.builder()
                .id(1L)
                .title("title")
                .producer("producer")
                .name("name")
                .kind("kind")
                .directorName("directorname")
                .role("role")
                .age("age")
                .shootingPeriod("shootinfperid")
                .pay("pay")
                .manager("manager")
                .email("email")
                .gender("gender")
                .deadline(date)
                .details("details")
                .build(), HttpStatus.OK);
        given(announcementService.findAllByUser(any(Pageable.class),any(HttpServletRequest.class))).willReturn(response);

        //when
        mvc.perform(get("/api/v1/user/announcement")
                .header("Authorization","fakeToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email"));
        verify(announcementService).findAllByUser(any(Pageable.class),any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("공고_수정_컨트롤러_테스트")
    void updateAnnouncementControllerTest() throws Exception {
        ResponseEntity<Message> response = new ResponseEntity<>(new Message("Success",3), HttpStatus.OK);
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
        given(announcementService.update(any(Long.class),any(AnnouncementRequestDto.class),any(HttpServletRequest.class))).willReturn(response);

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        //when
        mvc.perform(put("/api/v1/announcement/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(announcementRequestDto))
                .header("Authorization","fakeToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
        verify(announcementService).update(any(Long.class),any(AnnouncementRequestDto.class),any(HttpServletRequest.class));
    }
}
