//package com.objects.marketbridge.member.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.objects.marketbridge.common.config.KakaoPayConfig;
//import com.objects.marketbridge.common.infra.KakaoPayService;
//import com.objects.marketbridge.member.dto.CheckedResultDto;
//import com.objects.marketbridge.member.service.MemberService;
//import com.objects.marketbridge.common.security.config.SpringSecurityTestConfig;
//import com.objects.marketbridge.member.service.port.MembershipRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.*;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@ActiveProfiles("test")
//@WebMvcTest(MemberController.class)
//@AutoConfigureMockMvc
//@ContextConfiguration(classes = {SpringSecurityTestConfig.class})
//@ExtendWith(RestDocumentationExtension.class)
//public class MemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @MockBean
//    private MemberService memberService;
//    @MockBean
//    private KakaoPayService kakaoPayService;
//    @MockBean
//    private KakaoPayConfig kakaoPayConfig;
//    @MockBean
//    private MembershipRepository membershipRepository;
//
//
//    @BeforeEach
//    public void setUp(WebApplicationContext webApplicationContext,
//                      RestDocumentationContextProvider restDocumentationContextProvider) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentationContextProvider))
//                .build();
//    }
//

//import org.junit.jupiter.api.Disabled;

//      @Disabled
//    @Test
//    public void checkDuplicateEmail() throws Exception {
//        //given
//        String email = "member@example.com";
//        CheckedResultDto checkedResultDto = CheckedResultDto.builder().checked(true).build();
//        given(memberService.isDuplicateEmail(anyString()))
//                .willReturn(checkedResultDto);
//
//        //when
//        ResultActions actions = mockMvc.perform(get("/member/check-email")
//                .param("email", email)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        actions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.checked").value(checkedResultDto.getChecked()))
//                .andDo(document("member-check-email",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        queryParameters(
//                                parameterWithName("email").description("중복 체크할 이메일")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").type(JsonFieldType.NUMBER)
//                                        .description("코드"),
//                                fieldWithPath("status").type(JsonFieldType.STRING)
//                                        .description("상태"),
//                                fieldWithPath("message").type(JsonFieldType.STRING)
//                                        .description("메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT)
//                                        .description("응답 데이터"),
//                                fieldWithPath("data.checked").type(JsonFieldType.BOOLEAN)
//                                        .description("중복 체크 결과")
//                        )
//                ));
//    }
//}
