package com.myapp.warmwave.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.common.main.dto.MainInstDto;
import com.myapp.warmwave.domain.email.dto.RequestEmailAuthDto;
import com.myapp.warmwave.domain.user.dto.*;
import com.myapp.warmwave.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtAuthFilter.class
        })
})
@AutoConfigureRestDocs
@WithMockUser(roles = "USER")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("개인 회원가입 확인")
    @Test
    void addIndiv() throws Exception {
        // given
        RequestIndividualJoinDto reqDto = new RequestIndividualJoinDto(
                "test1@gmail.com", "a1234567", "개인", "서울 강남구 OO동", "서울", "강남구", "OO동"
        );

        ResponseUserJoinDto resDto = new ResponseUserJoinDto(
                1L, "test1@gmail.com", "z1234"
        );

        // when
        when(userService.joinIndividual(any())).thenReturn(resDto);

        // then
        mockMvc.perform(post("/api/users/register/individual")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("user/회원가입/개인"));
    }

    @DisplayName("기관 회원가입 확인")
    @Test
    void addInst() throws Exception {
        // given
        RequestInstitutionJoinDto reqDto = new RequestInstitutionJoinDto(
                "test1@gmail.com", "a1234567", "기관1", "1234567890", "서울 강남구 OO동", "서울", "강남구", "OO동"
        );

        ResponseUserJoinDto resDto = new ResponseUserJoinDto(
                2L, "test2@gmail.com", "z1234"
        );

        // when
        when(userService.joinInstitution(any())).thenReturn(resDto);

        // then
        mockMvc.perform(post("/api/users/register/institution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("user/회원가입/기관"));
    }

    @DisplayName("이메일 인증 확인")
    @Test
    @WithMockUser
    void checkEmail() throws Exception {
        // given
        RequestEmailAuthDto reqDto = new RequestEmailAuthDto(
                "test1@gmail.com", "z1234"
        );

        // when

        // then
        mockMvc.perform(get("/api/users/confirm-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andDo(document("user/이메일 인증"));
    }

    @DisplayName("로그인 확인")
    @Test
    void login() throws Exception {
        // given
        RequestUserLoginDto reqDto = new RequestUserLoginDto(
                "test1@gmail.com", "a1234567"
        );

        ResponseUserLoginDto resDto = new ResponseUserLoginDto(
                1L, "qwer", "asdf"
        );

        // when
        when(userService.loginUser(any(), any())).thenReturn(resDto);

        // then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/로그인"));
    }

    @DisplayName("개인 전체 조회 확인")
    @Test
    void readAllIndiv() throws Exception {
        // given
        ResponseUserDto resDto = new ResponseUserDto(
                1L, "개인", "test1@gmail.com", Role.INDIVIDUAL, 0F, true, "주소", 0, 0
        );

        List<ResponseUserDto> dtoList = List.of(resDto);

        // when
        when(userService.findAllByRoleIndividual()).thenReturn(dtoList);

        // then
        mockMvc.perform(get("/api/users/individual")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dtoList)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/전체 조회/개인"));
    }

    @DisplayName("기관 전체 조회 확인")
    @Test
    void readAllInst() throws Exception {
        // given
        ResponseUserDto resDto = new ResponseUserDto(
                2L, "기관1", "test2@gmail.com", Role.INSTITUTION, 0F, true, "주소", 0, 0
        );

        List<ResponseUserDto> dtoList = List.of(resDto);

        // when
        when(userService.findAllByRoleInstitution()).thenReturn(dtoList);

        // then
        mockMvc.perform(get("/api/users/institution")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dtoList)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/전체 조회/기관"));
    }

    @DisplayName("유저 조회 확인")
    @Test
    void readUser() throws Exception {
        // given
        Long userId = 1L;

        ResponseUserDto resDto = new ResponseUserDto(
                1L, "개인", "test1@gmail.com", Role.INDIVIDUAL, 0F, null, "주소", 0, 0
        );

        // when
        when(userService.findUser(any())).thenReturn(resDto);

        // then
        mockMvc.perform(get("/api/users/" + userId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/단일 조회"));
    }

    @DisplayName("개인 단일 조회 확인")
    @Test
    void readIndiv() throws Exception {
        // given
        Long userId = 1L;

        ResponseUserDto resDto = new ResponseUserDto(
                1L, "개인", "test1@gmail.com", Role.INDIVIDUAL, 0F, true, "주소", 0, 0
        );

        // when
        when(userService.findIndividual(any())).thenReturn(resDto);

        // then
        mockMvc.perform(get("/api/users/" + userId + "/individual")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/단일 조회/개인"));
    }

    @DisplayName("기관 단일 조회 확인")
    @Test
    void readInst() throws Exception {
        // given
        Long userId = 2L;

        ResponseUserDto resDto = new ResponseUserDto(
                2L, "기관1", "test2@gmail.com", Role.INSTITUTION, 0F, true, "주소", 0, 0
        );

        // when
        when(userService.findInstitution(any())).thenReturn(resDto);

        // then
        mockMvc.perform(get("/api/users/" + userId + "/institution")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/단일 조회/기관"));
    }

    @DisplayName("개인 정보 수정 확인")
    @Test
    void updateIndiv() throws Exception {
        // given
        Long userId = 1L;
        RequestIndividualUpdateDto reqDto = new RequestIndividualUpdateDto(
                "b1234567", "개인변경", "서울 성북구 XX동", "서울", "성북구", "XX동"
        );

        // when
        when(userService.updateIndiInfo(any(), any())).thenReturn(userId);

        // then
        mockMvc.perform(put("/api/users/" + userId + "/individual")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/정보 수정/개인"));
    }

    @DisplayName("기관 정보 수정 확인")
    @Test
    void updateInst() throws Exception {
        // given
        Long userId = 2L;
        RequestInstitutionUpdateDto reqDto = new RequestInstitutionUpdateDto(
                "b1234567", "서울 성북구 XX동", "서울", "성북구", "XX동"
        );

        // when
        when(userService.updateIndiInfo(any(), any())).thenReturn(userId);

        // then
        mockMvc.perform(put("/api/users/" + userId + "/institution")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/정보 수정/기관"));
    }

    @DisplayName("기관 가입 승인")
    @Test
    void approve() throws Exception {
        // given
        Long userId = 1L;

        // when

        // then
        mockMvc.perform(put("/api/users/" + userId + "/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("user/기관 가입 승인"));
    }

    @DisplayName("회원탈퇴 확인")
    @Test
    void delete() throws Exception {
        // given
        Long userId = 1L;

        // when

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/users/" + userId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("user/회원 탈퇴"));
    }

    @DisplayName("접속한 유저 근방 기관 조회")
    @Test
    void adjLocation() throws Exception {
        // given
        String email = "test1@gmail.com";
        int pageNum = 0;

        MainInstDto mainInstDto = new MainInstDto(
                2L, "기관명", "서울 강남구 OO동", "서울", "강남구", 0L
        );
        Page<MainInstDto> resDtoPage = new PageImpl<>(List.of(mainInstDto));

        // when
        when(userService.findAllByLocation(email, pageNum)).thenReturn(resDtoPage);

        // then
        mockMvc.perform(get("/api/users/adjacent")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/전체 조회/지역 기반"));
    }
}
