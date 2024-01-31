package com.objects.marketbridge.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.domain.AddressValue;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.CreateOrderHttp;
import com.objects.marketbridge.order.domain.Address;
import com.objects.marketbridge.order.domain.ProductValue;
import com.objects.marketbridge.order.service.CreateOrderService;
import com.objects.marketbridge.order.service.dto.CreateOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
@ExtendWith(RestDocumentationExtension.class)
public class OrderControllerRestDocsTest  {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean MemberRepository memberRepository;
    @MockBean KakaoPayConfig kakaoPayConfig;
    @MockBean CreateOrderService createOrderService;
    @MockBean KakaoPayService kakaoPayService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @DisplayName("checkout 화면에 필요한 데이터를 반환해준다")
    @Test
    @WithMockCustomUser
    void getCheckout() throws Exception {

        // given
        List<Address> addresses = getAddresses();
        Member member = createMember(addresses);
        when(memberRepository.findByIdWithAddresses(any())).thenReturn(member);

        // when, then
        mockMvc.perform(get("/orders/checkout")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-checkout",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("HTTP 응답"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.address").type(JsonFieldType.OBJECT)
                                        .description("배송지 정보"),

                                fieldWithPath("data.address.phoneNo").type(JsonFieldType.STRING)
                                        .description("수신인 핸드폰 번호"),
                                fieldWithPath("data.address.name").type(JsonFieldType.STRING)
                                        .description("수신인 이름"),
                                fieldWithPath("data.address.city").type(JsonFieldType.STRING)
                                        .description("시"),
                                fieldWithPath("data.address.street").type(JsonFieldType.STRING)
                                        .description("도로명"),
                                fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING)
                                        .description("우편번호"),
                                fieldWithPath("data.address.detail").type(JsonFieldType.STRING)
                                        .description("상세 주소"),
                                fieldWithPath("data.address.alias").type(JsonFieldType.STRING)
                                        .description("배송지 별칭")
                        )
                ));
    }

    private Member createMember(List<Address> addresses) {
        Member member = Member.builder()
                .id(1L)
                .email("member2@example.com")
                .password("1234")
                .name("홍길동")
                .build();

        // 연관관계 매핑
        for (Address addr : addresses) {
            addr.setMember(member);
        }

        return addresses.get(0).getMember();
    }

    private List<Address> getAddresses() {

        AddressValue addressValue = AddressValue.builder()
                .name("홍길동")
                .phoneNo("01012341234")
                .city("서울")
                .street("상도로")
                .zipcode("12345")
                .detail("101동 1212호")
                .alias("우리집")
                .build();

        Address defaultAddr = Address.builder().
                addressValue(addressValue)
                .isDefault(true).build();

        Address nonDefaultAddr1 = Address.builder().
                addressValue(addressValue)
                .isDefault(false).build();

        Address nonDefaultAddr2 = Address.builder().
                addressValue(addressValue)
                .isDefault(false).build();

        return List.of(defaultAddr, nonDefaultAddr1, nonDefaultAddr2);
    }

    @DisplayName("주문을 생성하는 API")
    @Test
    @WithMockCustomUser
    void createOrder() throws Exception {

        // given
        CreateOrderHttp.Request createOrderRequest = getCreateOrderRequest(createProductValues());
        KakaoPayReadyResponse response = KakaoPayReadyResponse.builder()
                .tid("tid")
                .nextRedirectPcUrl("nextRedirectPcUrl")
                .nextRedirectAppUrl("nextRedirectAppUrl")
                .nextRedirectMobileUrl("nextRedirectMobileUrl")
                .androidAppScheme("androidAppScheme")
                .iosAppScheme("iosAppScheme")
                .createdAt("createdAt")
                .build();

        given(kakaoPayService.ready(any(KakaoPayReadyRequest.class))).willReturn(response);
        willDoNothing().given(createOrderService).create(any(CreateOrderDto.class));

        // when, then
        mockMvc.perform(post("/orders/checkout")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                        .content(objectMapper.writeValueAsString(createOrderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                        .description("총 주문 금액"),
                                fieldWithPath("addressId").type(JsonFieldType.NUMBER)
                                        .description("배송지 ID"),
                                fieldWithPath("orderName").type(JsonFieldType.STRING)
                                        .description("주문 이름"),
                                fieldWithPath("orderName").type(JsonFieldType.STRING)
                                        .description("주문 이름"),
                                fieldWithPath("productValues").type(JsonFieldType.ARRAY)
                                        .description("주문 상품 정보"),

                                fieldWithPath("productValues[].productId").type(JsonFieldType.NUMBER)
                                        .description("상품 아이디"),
                                fieldWithPath("productValues[].couponId").type(JsonFieldType.NUMBER)
                                        .description("사용한 쿠폰 아이디"),
                                fieldWithPath("productValues[].quantity").type(JsonFieldType.NUMBER)
                                        .description("주문 상품 수량"),
                                fieldWithPath("productValues[].deliveredDate").type(JsonFieldType.STRING)
                                        .description("예상 배송 날짜")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("HTTP 응답"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.tid").type(JsonFieldType.STRING)
                                        .description("tid"),
                                fieldWithPath("data.nextRedirectPcUrl").type(JsonFieldType.STRING)
                                        .description("결제 고유 번호 (20자)"),
                                fieldWithPath("data.nextRedirectAppUrl").type(JsonFieldType.STRING)
                                        .description("APP용 인증 리다이렉트 URL"),
                                fieldWithPath("data.nextRedirectMobileUrl").type(JsonFieldType.STRING)
                                        .description("모바일 웹용 인증 리다이렉트 URL"),
                                fieldWithPath("data.androidAppScheme").type(JsonFieldType.STRING)
                                        .description("카카오페이 결제 화면으로 이동하는 Android 앱 스킴"),
                                fieldWithPath("data.iosAppScheme").type(JsonFieldType.STRING)
                                        .description("카카오페이 결제 화면으로 이동하는 iOS 앱 스킴"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .description("결제 준비 요청 시간")
                        )
                        ));
    }

    private CreateOrderHttp.Request getCreateOrderRequest(List<ProductValue> productValues) {
        return CreateOrderHttp.Request.builder()
                .amount(20000L)
                .addressId(1L)
                .orderName("가방외 1건")
                .productValues(productValues)
                .build();
    }

    private List<ProductValue> createProductValues() {
        ProductValue productValue1 = ProductValue.builder()
                .deliveredDate("2024-01-21")
                .couponId(1L)
                .productId(1L)
                .quantity(1L).build();

        ProductValue productValue2 = ProductValue.builder()
                .deliveredDate("2024-01-21")
                .couponId(2L)
                .productId(2L)
                .quantity(2L).build();

        return List.of(productValue1, productValue2);
    }
}
