package com.objects.marketbridge.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.domain.member.dto.CheckedResultDto;
import com.objects.marketbridge.domain.member.dto.SignInDto;
import com.objects.marketbridge.domain.member.dto.SignUpDto;
import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.global.security.SpringSecurityTestConfig;
import com.objects.marketbridge.global.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.global.security.dto.JwtTokenDto;
import com.objects.marketbridge.global.security.user.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SpringSecurityTestConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    public void checkDuplicateEmail() throws Exception {
        //given
        String email = "member@example.com";
        CheckedResultDto checkedResultDto = CheckedResultDto.builder().checked(true).build();
        given(memberService.isDuplicateEmail(anyString()))
                .willReturn(checkedResultDto);

        //when
        ResultActions actions = mockMvc.perform(get("/member/check-email")
                .param("email", email)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.checked").value(checkedResultDto.getChecked()))
                .andDo(document("member-check-email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("email").description("중복 체크할 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.checked").type(JsonFieldType.BOOLEAN)
                                        .description("중복 체크 결과")
                        )
                ));
    }

    @Test
    public void signUp() throws Exception {
        //given
        SignUpDto signUpDto = SignUpDto.builder()
                .email("member@example.com")
                .password("암호화_된_비밀번호")
                .name("테스트")
                .phoneNo("01000000000")
                .isAgree(true)
                .build();

        willDoNothing().given(memberService).save(signUpDto);

        //when
        ResultActions actions = mockMvc.perform(post("/member/sign-up")
                .content(objectMapper.writeValueAsString(signUpDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isCreated())
                .andDo(document("member-sign-up",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("중복 체크가 완료된 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("암호화 된 비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("phoneNo").type(JsonFieldType.STRING)
                                        .description("연락처"),
                                fieldWithPath("isAgree").type(JsonFieldType.BOOLEAN)
                                        .description("회원가입 동의 여부")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )
                ));
    }

    @Test
    public void signIn() throws Exception {
        //given
        SignInDto signInDto = SignInDto.builder()
                .email("member@example.com")
                .password("암호화_된_비밀번호")
                .build();

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .grantType("bearer")
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        given(memberService.signIn(any(SignInDto.class))).willReturn(jwtTokenDto);

        //when
        ResultActions actions = mockMvc.perform(post("/member/sign-in")
                .content(objectMapper.writeValueAsString(signInDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("member-sign-in",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("암호화 된 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.grantType").type(JsonFieldType.STRING)
                                        .description("토큰 타입: bearer"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("api 요청용 AccessToken"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .description("AccessToken 만료시 재발급용 Token 다른 api 요청 안됨")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    public void signOut() throws Exception {
        //given
        Long memberId = 1L;
        willDoNothing().given(memberService).signOut(memberId);

        //when
        ResultActions actions = mockMvc.perform(delete("/member/sign-out")
                .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("member-sign-out",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )
                ));

    }

    @Test
    @WithMockCustomUser
    public void reIssueToken() throws Exception {
        //given
        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .grantType("bearer")
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        given(memberService.reIssueToken(any(CustomUserDetails.class)))
                .willReturn(jwtTokenDto);

        //when
        ResultActions actions = mockMvc.perform(put("/member/re-issue")
                .header(HttpHeaders.AUTHORIZATION, "bearer RefreshToken")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("member-re-issue",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.grantType").type(JsonFieldType.STRING)
                                        .description("토큰 타입: bearer"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("api 요청용 AccessToken"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .description("AccessToken 만료시 재발급용 Token 다른 api 요청 안됨")
                        )
                ));
    }


}
