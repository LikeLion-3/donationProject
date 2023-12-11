package com.myapp.warmwave.domain.favorite.controller;

import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.config.security.SecurityConfig;
import com.myapp.warmwave.domain.favorite_inst.controller.FavoriteInstController;
import com.myapp.warmwave.domain.favorite_inst.dto.FavoriteInstDto;
import com.myapp.warmwave.domain.favorite_inst.dto.ResponseFavoriteDto;
import com.myapp.warmwave.domain.favorite_inst.service.FavoriteInstService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FavoriteInstController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthFilter.class})
})
public class FavoriteInstControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteInstService favoriteInstService;

    @DisplayName("관심 기관 등록 확인")
    @Test
    void addFavorite() throws Exception {
        // given
        Long institutionId = 2L;
        String email = "test1@gmail.com";
        ResponseFavoriteDto resDto = new ResponseFavoriteDto(1L, 2L);

        // when
        when(favoriteInstService.createFavoriteInst(institutionId, email)).thenReturn(resDto);

        // then
        mockMvc.perform(post("/api/users/" + institutionId + "/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("test1@gmail.com").password("1234").roles("INDIVIDUAL")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("관심 기관 목록 확인")
    @Test
    void readAll() throws Exception {
        // given
        Long individualId = 1L;
        FavoriteInstDto resDto = new FavoriteInstDto(
                2L, "기관명1", "XX"
        );

        List<FavoriteInstDto> dtoList = List.of(resDto);

        // when
        when(favoriteInstService.findAllFavoriteInstByIndividual(individualId))
                .thenReturn(dtoList);

        // then
        mockMvc.perform(get("/api/users/" + individualId + "/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("test1@gmail.com")
                .password("1234")
                .roles("INDIVIDUAL"))).andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("관심 기관 삭제 확인")
    @Test
    void delete() throws Exception {
        // given
        Long institutionId = 2L;
//        String email = "test1@gmail.com";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + institutionId + "/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("test1@gmail.com").password("1234").roles("INDIVIDUAL")))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
