package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.order.RestDocsSupport;
import com.objects.marketbridge.domain.order.controller.request.CheckoutRequest;
import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerRestDocsTest extends RestDocsSupport {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final TossPaymentConfig tossPaymentConfig = mock(TossPaymentConfig.class);
    private final CreateOrderService createOrderService = mock(CreateOrderService.class);
    @Override
    protected Object initController() {
        return new OrderController(memberRepository, tossPaymentConfig, createOrderService);
    }

    @DisplayName("주문을 생성하는 API")
    @Test
    void createOrder() throws Exception {

        // given
        List<ProductValue> productValues = createProductValues();
        CheckoutRequest request = createCheckoutRequest(productValues);

        given(tossPaymentConfig.getSuccessUrl()).willReturn("/success");
        given(tossPaymentConfig.getFailUrl()).willReturn("/fail");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders/checkout")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderId").type(JsonFieldType.STRING)
                                        .description("주문 번호"),
                                fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                        .description("총 주문 금액"),
                                fieldWithPath("addressId").type(JsonFieldType.NUMBER)
                                        .description("배송지 ID"),
                                fieldWithPath("orderName").type(JsonFieldType.STRING)
                                        .description("주문 이름"),
                                fieldWithPath("successUrl").type(JsonFieldType.NULL)
                                        .description("인증 성공 리다이렉트 URL"),
                                fieldWithPath("failUrl").type(JsonFieldType.NULL)
                                        .description("인증 실패 리다이렉트 URL"),
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
                                fieldWithPath("data.successUrl").type(JsonFieldType.STRING)
                                        .description("인증 성공 리다이렉트 URL"),
                                fieldWithPath("data.failUrl").type(JsonFieldType.STRING)
                                        .description("인증 실패 리다이렉트 URL"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),

                                fieldWithPath("data.orderId").type(JsonFieldType.STRING)
                                        .description("주문 번호"),
                                fieldWithPath("data.amount").type(JsonFieldType.NUMBER)
                                        .description("총 주문 금액"),
                                fieldWithPath("data.addressId").type(JsonFieldType.NUMBER)
                                        .description("배송지 ID"),
                                fieldWithPath("data.orderName").type(JsonFieldType.STRING)
                                        .description("주문 이름"),
                                fieldWithPath("data.productValues").type(JsonFieldType.ARRAY)
                                        .description("주문 상품 정보"),

                                fieldWithPath("data.productValues[].productId").type(JsonFieldType.NUMBER)
                                        .description("상품 아이디"),
                                fieldWithPath("data.productValues[].couponId").type(JsonFieldType.NUMBER)
                                        .description("사용한 쿠폰 아이디"),
                                fieldWithPath("data.productValues[].quantity").type(JsonFieldType.NUMBER)
                                        .description("주문 상품 수량"),
                                fieldWithPath("data.productValues[].deliveredDate").type(JsonFieldType.STRING)
                                        .description("예상 배송 날짜")
                        )
                        ));


    }

    private CheckoutRequest createCheckoutRequest(List<ProductValue> productValues) {
        return CheckoutRequest.builder()
                .addressId(1L)
                .orderId("aaaa-aaaa-aaaa")
                .orderName("가방외 1건")
                .amount(20000L)
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
