package com.objects.marketbridge.order.controller.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.order.controller.OrderController;
import com.objects.marketbridge.order.controller.dto.CreateCheckoutHttp;
import com.objects.marketbridge.order.controller.dto.CreateOrderHttp;
import com.objects.marketbridge.order.domain.ProductValue;
import com.objects.marketbridge.order.service.CreateCheckoutService;
import com.objects.marketbridge.order.service.CreateOrderService;
import com.objects.marketbridge.order.service.GetOrderService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean CreateCheckoutService createCheckoutService;
    @MockBean CreateOrderService createOrderService;
    @MockBean KakaoPayConfig kakaoPayConfig;
    @MockBean KakaoPayService kakaoPayService;
    @MockBean GetOrderService getOrderService;


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
        CreateCheckoutHttp.Response response = CreateCheckoutHttp.Response.builder()
                .phoneNo("010-1234-1234")
                .name("홍길동")
                .city("서울")
                .street("세종대로")
                .zipcode("12345")
                .detail("민들레 아파트 110동 1234호")
                .alias("우리집")
                .build();

        given(createCheckoutService.create(anyLong())).willReturn(response);
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

                                fieldWithPath("data.phoneNo").type(JsonFieldType.STRING)
                                        .description("수신인 핸드폰 번호"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("수신인 이름"),
                                fieldWithPath("data.city").type(JsonFieldType.STRING)
                                        .description("시"),
                                fieldWithPath("data.street").type(JsonFieldType.STRING)
                                        .description("도로명"),
                                fieldWithPath("data.zipcode").type(JsonFieldType.STRING)
                                        .description("우편번호"),
                                fieldWithPath("data.detail").type(JsonFieldType.STRING)
                                        .description("상세 주소"),
                                fieldWithPath("data.alias").type(JsonFieldType.STRING)
                                        .description("배송지 별칭")
                        )
                ));
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
                                fieldWithPath("productValues[].sellerId").type(JsonFieldType.NUMBER)
                                        .description("판매자 아이디"),
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
                .sellerId(1L)
                .couponId(1L)
                .productId(1L)
                .quantity(1L).build();

        ProductValue productValue2 = ProductValue.builder()
                .deliveredDate("2024-01-21")
                .sellerId(2L)
                .couponId(2L)
                .productId(2L)
                .quantity(2L).build();

        return List.of(productValue1, productValue2);
    }
}
