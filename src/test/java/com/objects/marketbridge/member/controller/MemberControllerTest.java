package com.objects.marketbridge.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.common.security.config.SpringSecurityTestConfig;
import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.member.dto.*;
import com.objects.marketbridge.member.service.MemberService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SpringSecurityTestConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
        ResultActions actions = mockMvc.perform(get("/member/email-check")
                .param("email", email)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.checked").value(checkedResultDto.getChecked()))
                .andDo(document("member-email-check",
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
    public void testFindAddress() throws Exception{
        //given
        AddressValue addressValue1 = AddressValue.builder()
                .phoneNo("12312341234")
                .name("집")
                .city("인천")
                .street("소래역남로 40")
                .zipcode("12345")
                .detail("C동 307호")
                .alias("우리집").build();

        AddressValue addressValue2 = AddressValue.builder()
                .phoneNo("56756785678")
                .name("회사")
                .city("서울")
                .street("강남대로 123")
                .zipcode("54321")
                .detail("A동 101호")
                .alias("회사").build();

        List<GetAddressesResponse> responses = Arrays.asList(
                GetAddressesResponse.builder()
                        .addressValue(addressValue1)
                        .addressId(1004L)
                        .isDefault(true)
                        .build(),
                GetAddressesResponse.builder()
                        .addressValue(addressValue2)
                        .addressId(1005L)
                        .isDefault(false)
                        .build()
        );
        //when
        given(memberService.findByMemberId(any())).willReturn(responses);

        //then
        mockMvc.perform(get("/member/find-address")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("find-address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("HTTP 응답"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("주소 목록"),
                                fieldWithPath("data[].addressId").type(JsonFieldType.NUMBER)
                                        .description("주소 ID"),
                                fieldWithPath("data[].addressValue").type(JsonFieldType.OBJECT)
                                        .description("주소 정보"),
                                fieldWithPath("data[].addressValue.phoneNo").type(JsonFieldType.STRING)
                                        .description("전화번호"),
                                fieldWithPath("data[].addressValue.name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("data[].addressValue.city").type(JsonFieldType.STRING)
                                        .description("도시"),
                                fieldWithPath("data[].addressValue.street").type(JsonFieldType.STRING)
                                        .description("거리"),
                                fieldWithPath("data[].addressValue.zipcode").type(JsonFieldType.STRING)
                                        .description("우편번호"),
                                fieldWithPath("data[].addressValue.detail").type(JsonFieldType.STRING)
                                        .description("상세주소"),
                                fieldWithPath("data[].addressValue.alias").type(JsonFieldType.STRING)
                                        .description("별칭"),
                                fieldWithPath("data[].isDefault").type(JsonFieldType.BOOLEAN)
                                        .description("기본 주소 여부")
                        )
                ));
     }

     @Test
     void addAddress() throws Exception{
        //given
         AddressValue addressValue1 = AddressValue.builder()
                 .phoneNo("12312341234")
                 .name("집")
                 .city("인천")
                 .street("소래역남로 40")
                 .zipcode("12345")
                 .detail("C동 307호")
                 .alias("우리집").build();

         AddressValue addressValue2 = AddressValue.builder()
                 .phoneNo("56756785678")
                 .name("회사")
                 .city("서울")
                 .street("강남대로 123")
                 .zipcode("54321")
                 .detail("A동 101호")
                 .alias("회사").build();

         AddressValue addressValue3 = AddressValue.builder()
                 .phoneNo("1234567890")
                 .name("식당")
                 .city("서울")
                 .street("123 Main St")
                 .zipcode("12345")
                 .detail("Apt 101")
                 .alias("단골집").build();

         List<GetAddressesResponse> responses = Arrays.asList(
                 GetAddressesResponse.builder()
                         .addressValue(addressValue1)
                         .addressId(1004L)
                         .isDefault(true)
                         .build(),
                 GetAddressesResponse.builder()
                         .addressValue(addressValue2)
                         .addressId(1005L)
                         .isDefault(false)
                         .build(),
                 GetAddressesResponse.builder()
                         .addressValue(addressValue3)
                         .addressId(1006L)
                         .isDefault(false)
                         .build()
         );

         String requestBody = "{\"addressValue\": {\"phoneNo\": \"1234567890\", \"name\": \"식당\", \"city\": \"서울\", \"street\": \"123 Main St\", \"zipcode\": \"12345\", \"detail\": \"Apt 101\", \"alias\": \"단골집\"}, \"isDefault\": false}";


         // when ,then
         given(memberService.addMemberAddress(any(),any())).willReturn(responses);

         mockMvc.perform(post("/member/add-address")
                         .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(requestBody))
                 .andExpect(status().isOk())
                 .andDo(print())
                 .andDo(document("add-address",
                         preprocessRequest(prettyPrint()),
                         preprocessResponse(prettyPrint()),
                         requestFields(
                                 fieldWithPath("addressValue.phoneNo").type(JsonFieldType.STRING)
                                         .description("전화번호"),
                                 fieldWithPath("addressValue.name").type(JsonFieldType.STRING)
                                         .description("이름"),
                                 fieldWithPath("addressValue.city").type(JsonFieldType.STRING)
                                         .description("도시"),
                                 fieldWithPath("addressValue.street").type(JsonFieldType.STRING)
                                         .description("거리"),
                                 fieldWithPath("addressValue.zipcode").type(JsonFieldType.STRING)
                                         .description("우편번호"),
                                 fieldWithPath("addressValue.detail").type(JsonFieldType.STRING)
                                         .description("상세주소"),
                                 fieldWithPath("addressValue.alias").type(JsonFieldType.STRING)
                                         .description("별칭"),
                                 fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                                         .description("기본 주소 여부")
                         ),
                         responseFields(
                                 fieldWithPath("code").type(JsonFieldType.NUMBER)
                                         .description("응답 코드"),
                                 fieldWithPath("status").type(JsonFieldType.STRING)
                                         .description("HTTP 응답"),
                                 fieldWithPath("message").type(JsonFieldType.STRING)
                                         .description("응답 메시지"),
                                 fieldWithPath("data").type(JsonFieldType.ARRAY)
                                         .description("주소 목록").optional(),
                                 fieldWithPath("data[].addressId").type(JsonFieldType.NUMBER)
                                         .description("주소 ID"),
                                 fieldWithPath("data[].addressValue").type(JsonFieldType.OBJECT)
                                         .description("주소 정보"),
                                 fieldWithPath("data[].addressValue.phoneNo").type(JsonFieldType.STRING)
                                         .description("전화번호"),
                                 fieldWithPath("data[].addressValue.name").type(JsonFieldType.STRING)
                                         .description("이름"),
                                 fieldWithPath("data[].addressValue.city").type(JsonFieldType.STRING)
                                         .description("도시"),
                                 fieldWithPath("data[].addressValue.street").type(JsonFieldType.STRING)
                                         .description("거리"),
                                 fieldWithPath("data[].addressValue.zipcode").type(JsonFieldType.STRING)
                                         .description("우편번호"),
                                 fieldWithPath("data[].addressValue.detail").type(JsonFieldType.STRING)
                                         .description("상세주소"),
                                 fieldWithPath("data[].addressValue.alias").type(JsonFieldType.STRING)
                                         .description("별칭"),
                                 fieldWithPath("data[].isDefault").type(JsonFieldType.BOOLEAN)
                                         .description("기본 주소 여부")
                         )
                 ));
     }

     @Test
     void updateAddress() throws Exception{
        //given
         AddressValue addressValue1 = AddressValue.builder()
                 .phoneNo("12312341234")
                 .name("집")
                 .city("인천")
                 .street("소래역남로 40")
                 .zipcode("12345")
                 .detail("C동 307호")
                 .alias("우리집").build();

         AddressValue addressValue2 = AddressValue.builder()
                 .phoneNo("56756785678")
                 .name("회사")
                 .city("서울")
                 .street("강남대로 123")
                 .zipcode("54321")
                 .detail("A동 101호")
                 .alias("회사").build();

         AddressValue addressValue3 = AddressValue.builder()
                 .phoneNo("111111111")
                 .name("줄서는식당")
                 .city("서울")
                 .street("메인로 123")
                 .zipcode("12345")
                 .detail("101호")
                 .alias("회식장소").build();

         List<GetAddressesResponse> responses = Arrays.asList(
                 GetAddressesResponse.builder()
                         .addressValue(addressValue1)
                         .addressId(1004L)
                         .isDefault(true)
                         .build(),
                 GetAddressesResponse.builder()
                         .addressValue(addressValue2)
                         .addressId(1005L)
                         .isDefault(false)
                         .build(),
                 GetAddressesResponse.builder()
                         .addressValue(addressValue3)
                         .addressId(1006L)
                         .isDefault(false)
                         .build()
         );


         String requestBody = "{\"addressValue\": {\"phoneNo\": \"111111111\", \"name\": \"줄서는식당\", \"city\": \"서울\", \"street\": \"메인로 123\", \"zipcode\": \"12345\", \"detail\": \"101호\", \"alias\": \"회식장소\"}, \"isDefault\": false}";

         //when
         given(memberService.updateMemberAddress(any(),any(),any())).willReturn(responses);

        //then
         String addressId = "1006"; // 예시로 사용할 주문 번호를 지정합니다.
         mockMvc.perform(RestDocumentationRequestBuilders.patch("/member/update-address/{addressId}",addressId)
                         .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(requestBody))
                 .andExpect(status().isOk())
                 .andDo(print())
                 .andDo(document("update-address",
                         preprocessRequest(prettyPrint()),
                         preprocessResponse(prettyPrint()),
                         pathParameters(
                                 parameterWithName("addressId")
                                         .description("수정할 주소 id")
                         ),
                         requestFields(
                                 fieldWithPath("addressValue.phoneNo").type(JsonFieldType.STRING)
                                         .description("전화번호"),
                                 fieldWithPath("addressValue.name").type(JsonFieldType.STRING)
                                         .description("이름"),
                                 fieldWithPath("addressValue.city").type(JsonFieldType.STRING)
                                         .description("도시"),
                                 fieldWithPath("addressValue.street").type(JsonFieldType.STRING)
                                         .description("거리"),
                                 fieldWithPath("addressValue.zipcode").type(JsonFieldType.STRING)
                                         .description("우편번호"),
                                 fieldWithPath("addressValue.detail").type(JsonFieldType.STRING)
                                         .description("상세주소"),
                                 fieldWithPath("addressValue.alias").type(JsonFieldType.STRING)
                                         .description("별칭"),
                                 fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                                         .description("기본 주소 여부")
                         ),
                         responseFields(
                                 fieldWithPath("code").type(JsonFieldType.NUMBER)
                                         .description("응답 코드"),
                                 fieldWithPath("status").type(JsonFieldType.STRING)
                                         .description("HTTP 응답"),
                                 fieldWithPath("message").type(JsonFieldType.STRING)
                                         .description("응답 메시지"),
                                 fieldWithPath("data").type(JsonFieldType.ARRAY)
                                         .description("주소 목록").optional(),
                                 fieldWithPath("data[].addressId").type(JsonFieldType.NUMBER)
                                         .description("주소 ID"),
                                 fieldWithPath("data[].addressValue").type(JsonFieldType.OBJECT)
                                         .description("주소 정보"),
                                 fieldWithPath("data[].addressValue.phoneNo").type(JsonFieldType.STRING)
                                         .description("전화번호"),
                                 fieldWithPath("data[].addressValue.name").type(JsonFieldType.STRING)
                                         .description("이름"),
                                 fieldWithPath("data[].addressValue.city").type(JsonFieldType.STRING)
                                         .description("도시"),
                                 fieldWithPath("data[].addressValue.street").type(JsonFieldType.STRING)
                                         .description("거리"),
                                 fieldWithPath("data[].addressValue.zipcode").type(JsonFieldType.STRING)
                                         .description("우편번호"),
                                 fieldWithPath("data[].addressValue.detail").type(JsonFieldType.STRING)
                                         .description("상세주소"),
                                 fieldWithPath("data[].addressValue.alias").type(JsonFieldType.STRING)
                                         .description("별칭"),
                                 fieldWithPath("data[].isDefault").type(JsonFieldType.BOOLEAN)
                                         .description("기본 주소 여부")
                         )
                 ));
     }

     @Test
     void deleteAddress() throws Exception{
        //given
         AddressValue addressValue1 = AddressValue.builder()
                 .phoneNo("12312341234")
                 .name("집")
                 .city("인천")
                 .street("소래역남로 40")
                 .zipcode("12345")
                 .detail("C동 307호")
                 .alias("우리집").build();

         AddressValue addressValue2 = AddressValue.builder()
                 .phoneNo("56756785678")
                 .name("회사")
                 .city("서울")
                 .street("강남대로 123")
                 .zipcode("54321")
                 .detail("A동 101호")
                 .alias("회사").build();

         List<GetAddressesResponse> responses = Arrays.asList(
                 GetAddressesResponse.builder()
                         .addressValue(addressValue1)
                         .addressId(1004L)
                         .isDefault(true)
                         .build(),
                 GetAddressesResponse.builder()
                         .addressValue(addressValue2)
                         .addressId(1005L)
                         .isDefault(false)
                         .build()
         );

        //when
         given(memberService.deleteMemberAddress(any(),any())).willReturn(responses);


        //then
         String addressId = "1006";
         mockMvc.perform(delete("/member/delete-address/{addressId}", addressId)
                         .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                         .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andDo(print())
                 .andDo(document("delete-address",
                         preprocessRequest(prettyPrint()),
                         preprocessResponse(prettyPrint()),
                         pathParameters(
                                 parameterWithName("addressId")
                                         .description("삭제할 주소 id")
                         ),
                         responseFields(
                                 fieldWithPath("code").type(JsonFieldType.NUMBER)
                                         .description("응답 코드"),
                                 fieldWithPath("status").type(JsonFieldType.STRING)
                                         .description("HTTP 응답"),
                                 fieldWithPath("message").type(JsonFieldType.STRING)
                                         .description("응답 메시지"),
                                 fieldWithPath("data").type(JsonFieldType.ARRAY)
                                         .description("주소 목록").optional(),
                                 fieldWithPath("data[].addressId").type(JsonFieldType.NUMBER)
                                         .description("주소 ID"),
                                 fieldWithPath("data[].addressValue").type(JsonFieldType.OBJECT)
                                         .description("주소 정보"),
                                 fieldWithPath("data[].addressValue.phoneNo").type(JsonFieldType.STRING)
                                         .description("전화번호"),
                                 fieldWithPath("data[].addressValue.name").type(JsonFieldType.STRING)
                                         .description("이름"),
                                 fieldWithPath("data[].addressValue.city").type(JsonFieldType.STRING)
                                         .description("도시"),
                                 fieldWithPath("data[].addressValue.street").type(JsonFieldType.STRING)
                                         .description("거리"),
                                 fieldWithPath("data[].addressValue.zipcode").type(JsonFieldType.STRING)
                                         .description("우편번호"),
                                 fieldWithPath("data[].addressValue.detail").type(JsonFieldType.STRING)
                                         .description("상세주소"),
                                 fieldWithPath("data[].addressValue.alias").type(JsonFieldType.STRING)
                                         .description("별칭"),
                                 fieldWithPath("data[].isDefault").type(JsonFieldType.BOOLEAN)
                                         .description("기본 주소 여부")
                         )
                 ));
     }


   @Test
   @WithMockCustomUser
   public void getEmail() throws Exception {
       //given
       Long memberId = 1L;
       MemberEmail memberEmail = MemberEmail.builder().email("test@test.com").build();

       given(memberService.getEmail(memberId)).willReturn(memberEmail);

       //when
       ResultActions actions = mockMvc.perform(get("/member/account-email")
               .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
               .contentType(MediaType.APPLICATION_JSON));

       //then
       actions.andExpect(status().isOk())
               .andDo(document("member-account-email",
                       preprocessRequest(prettyPrint()),
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
                               fieldWithPath("data.email").type(JsonFieldType.STRING)
                                       .description("로그인 이메일")
                       )
               ));
   }

    @Test
    @WithMockCustomUser
    public void getMemberInfo() throws Exception {
        //given
        Long memberId = 1L;
        String password = "password";
        GetMemberInfo memberInfo = GetMemberInfo.builder()
                .email("test@test.com")
                .name("테스트")
                .phoneNo("01012341234")
                .isAlert(true)
                .isAgree(true)
                .build();

        given(memberService.getMemberInfo(memberId, password)).willReturn(memberInfo);

        //when
        ResultActions actions = mockMvc.perform(get("/member/account-info")
                .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("get-member-account-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("password").description("암호화된 비밀번호")
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
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("로그인 이메일"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("data.phoneNo").type(JsonFieldType.STRING)
                                        .description("휴대폰 번호"),
                                fieldWithPath("data.isAlert").type(JsonFieldType.BOOLEAN)
                                        .description("알림 동의 여부"),
                                fieldWithPath("data.isAgree").type(JsonFieldType.BOOLEAN)
                                        .description("마케팅 수신 동의 여부")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    public void updateMemberInfo() throws Exception {
        //given
        Long memberId = 1L;
        UpdateMemberInfo updateMemberInfo = UpdateMemberInfo.builder()
                .email("test@test.com")
                .name("테스트")
                .phoneNo("01012341234")
                .password("password")
                .isAlert(true)
                .isAgree(true)
                .build();

        willDoNothing().given(memberService).updateMemberInfo(memberId, updateMemberInfo);

        //when
        ResultActions actions = mockMvc.perform(patch("/member/account-info")
                .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                .content(objectMapper.writeValueAsString(updateMemberInfo))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("patch-member-account-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("암호화 된 비밀번호"),
                                fieldWithPath("phoneNo").type(JsonFieldType.STRING)
                                        .description("휴대폰 번호"),
                                fieldWithPath("isAlert").type(JsonFieldType.BOOLEAN)
                                        .description("알림 동의 여부"),
                                fieldWithPath("isAgree").type(JsonFieldType.BOOLEAN)
                                        .description("마케팅 수신 동의 여부")
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
    public void findMemberEmail() throws Exception {
        //given;
        String name = "테스트";
        String phoneNo = "01000000000";
        MemberEmail memberEmail = MemberEmail.builder().email("test@test.com").build();

        given(memberService.findMemberEmail(name, phoneNo)).willReturn(memberEmail);

        //when
        ResultActions actions = mockMvc.perform(get("/member/email-find")
                .param("name", name).param("phoneNo", phoneNo)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("member-email-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("name").description("이름"),
                                parameterWithName("phoneNo").description("휴대폰 번호")
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
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("가입된 멤버의 이메일(아이디)")
                        )
                ));
    }

    @Test
    public void findMemberId() throws Exception {
        //given;
        String name = "테스트";
        String email = "test@test.com";
        MemberId memberId = MemberId.builder()
                .memberId(1L)
                .build();

        given(memberService.findMemberId(name, email)).willReturn(memberId);

        //when
        ResultActions actions = mockMvc.perform(get("/member/password-find")
                .param("name", name).param("email", email)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("member-password-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("name").description("이름"),
                                parameterWithName("email").description("이메일")
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
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                        .description("가입된 멤버의 아이디")

                        )
                ));
    }

    @Test
    public void updatePassword() throws Exception {
        //given
        UpdatePassword updatePassword = UpdatePassword.builder()
                .memberId(1L)
                .password("password")
                .build();

        willDoNothing().given(memberService).updatePassword(updatePassword);

        //when
        ResultActions actions = mockMvc.perform(patch("/member/password-reset")
                .content(objectMapper.writeValueAsString(updatePassword))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andDo(document("member-password-reset",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("GET /member/account-find 찾은 멤버 아이디"),
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
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )
                ));
    }
}
