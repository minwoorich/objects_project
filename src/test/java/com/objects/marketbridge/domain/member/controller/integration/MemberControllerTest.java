package com.objects.marketbridge.domain.member.controller.integration;

import com.objects.marketbridge.domain.member.controller.MemberController;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.global.security.SpringSecurityTestConfig;
import com.objects.marketbridge.global.security.annotation.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SpringSecurityTestConfig.class})
public class MemberControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private MemberService memberService;
    @MockBean private MemberRepository memberRepository;

    @Test
    @WithMockCustomUser
    public void test() throws Exception {
        //given

        //when

        //then
        mockMvc.perform(get("/member/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    public void reIssueToken() throws Exception {
        //given
        //when
//        JwtTokenDto mockedTokenDto = JwtTokenDto.builder().build();
//        when(memberService.reIssueToken(any(CustomUserDetails.class)))
//                .thenReturn(mockedTokenDto);
        //then
        mockMvc.perform(put("/member/re-issue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    public void signOut() throws Exception {
        //given

        //when
//        doNothing().when(memberService).signOut(anyLong());
        //then
        mockMvc.perform(delete("/member/sign-out"))
                .andExpect(status().isOk());
    }

}
