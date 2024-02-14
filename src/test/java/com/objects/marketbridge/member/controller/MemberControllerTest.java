package com.objects.marketbridge.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.security.config.SpringSecurityTestConfig;
import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.dto.GetAddressesResponse;
import com.objects.marketbridge.member.service.MemberService;
import com.objects.marketbridge.member.service.port.MemberRepository;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
    @MockBean
    private MemberRepository memberRepository;

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
}
