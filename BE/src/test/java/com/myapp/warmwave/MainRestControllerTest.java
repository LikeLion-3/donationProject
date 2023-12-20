package com.myapp.warmwave;

import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.common.main.controller.MainRestController;
import com.myapp.warmwave.common.main.dto.MainDto;
import com.myapp.warmwave.common.main.service.MainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {MainRestController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtAuthFilter.class
        })
})
@AutoConfigureRestDocs
@WithMockUser(roles = "USER")
class MainRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainService mainService;

    @DisplayName("메인 통계")
    @Test
    void mainCount() throws Exception {
        // given
        MainDto resDto = new MainDto();

        // when
        when(mainService.getInfo()).thenReturn(resDto);

        // then
        mockMvc.perform(get("/api/main/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("main/통계"));
    }
}
