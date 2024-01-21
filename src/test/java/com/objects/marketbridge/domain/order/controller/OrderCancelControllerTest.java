package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.RestDocsSupport;
import com.objects.marketbridge.domain.order.controller.request.OrderCancelRequest;
import com.objects.marketbridge.domain.order.controller.response.*;
import com.objects.marketbridge.domain.order.dto.OrderCancelServiceDto;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.entity.ProdOrderDetail;
import com.objects.marketbridge.domain.order.service.OrderCancelReturnService;
import com.objects.marketbridge.domain.payment.dto.RefundDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderCancelControllerTest extends RestDocsSupport {

    private final OrderCancelReturnService orderCancelService = mock(OrderCancelReturnService.class);

    @Override
    protected Object initController() {
        return new OrderCancelController(orderCancelService);
    }

    @Test
    @DisplayName("주문 취소 확정 API")
    public void cancelOrder() throws Exception {
        // given
        OrderCancelServiceDto request = OrderCancelRequest.builder()
                .orderId(1L)
                .cancelReason("빵빵아! 옥지얌!")
                .build()
                .toServiceRequest();

        given(orderCancelService.cancelReturnOrder(any(OrderCancelServiceDto.class), any(LocalDateTime.class)))
                .willReturn(OrderCancelReturnResponse.builder()
                        .orderId(1L)
                        .orderNumber("ORD001")
                        .totalPrice(300L)
                        .cancellationDate(LocalDateTime.of(2024, 1, 18, 12, 26))
                        .refundInfo(RefundInfo.of(
                                RefundDto.builder()
                                        .totalRefundAmount(10000L)
                                        .refundMethod("card")
                                        .refundProcessedAt(LocalDateTime.of(2024, 1, 18, 12, 26))
                                        .build())
                        )
                        .cancelledItems(List.of(
                                        ProductResponse.builder()
                                                .productId(1L)
                                                .name("빵빵이 키링")
                                                .productNo("P123456")
                                                .price(10000L)
                                                .build(),
                                        ProductResponse.builder()
                                                .productId(2L)
                                                .name("옥지얌 키링")
                                                .productNo("P2345667")
                                                .price(20000L)
                                                .build()
                                )
                        )
                        .build()
                );

        // when // then
        mockMvc.perform(
                        post("/orders/cancel-return-flow/thank-you")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-cancel-return",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderId").type(JsonFieldType.NUMBER)
                                        .description("상품 타입"),
                                fieldWithPath("cancelReason").type(JsonFieldType.STRING)
                                        .description("상품 판매상태")
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
                                fieldWithPath("data.orderId").type(JsonFieldType.NUMBER)
                                        .description("주문 ID"),
                                fieldWithPath("data.orderNumber").type(JsonFieldType.STRING)
                                        .description("주문 번호"),
                                fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER)
                                        .description("주문 총 가격"),
                                fieldWithPath("data.cancellationDate").type(JsonFieldType.ARRAY)
                                        .description("주문 취소 시간"),
                                fieldWithPath("data.cancelledItems").type(JsonFieldType.ARRAY)
                                        .description("상품 이름"),
                                fieldWithPath("data.cancelledItems[].productId").type(JsonFieldType.NUMBER)
                                        .description("취소된 상품의 제품 ID"),
                                fieldWithPath("data.cancelledItems[].productNo").type(JsonFieldType.STRING)
                                        .description("취소된 상품의 제품 번호"),
                                fieldWithPath("data.cancelledItems[].name").type(JsonFieldType.STRING)
                                        .description("취소된 상품의 이름"),
                                fieldWithPath("data.cancelledItems[].price").type(JsonFieldType.NUMBER)
                                        .description("취소된 상품의 가격"),
                                fieldWithPath("data.refundInfo").type(JsonFieldType.OBJECT)
                                        .description("환불 정보"),
                                fieldWithPath("data.refundInfo.totalRefundAmount").type(JsonFieldType.NUMBER)
                                        .description("환불 총 금액"),
                                fieldWithPath("data.refundInfo.refundMethod").type(JsonFieldType.STRING)
                                        .description("환불 방법"),
                                fieldWithPath("data.refundInfo.refundProcessedAt").type(JsonFieldType.ARRAY)
                                        .description("환불 시간")
                        )
                ));
    }

    @Test
    @DisplayName("")
    public void requestCancelOrder() throws Exception {
        // given
        Long orderId = 1L;
        List<Long> productIds = List.of(1L, 2L, 3L);

        Product product1 = Product.builder()
                .price(1000L)
                .thumbImg("썸네일1")
                .name("옷")
                .build();
        Product product2 = Product.builder()
                .name("바지")
                .price(2000L)
                .thumbImg("썸네일2")
                .build();
        Product product3 = Product.builder()
                .name("신발")
                .price(3000L)
                .thumbImg("썸네일3")
                .build();

        ProdOrderDetail orderDetail1 = ProdOrderDetail.builder()
                .product(product1)
                .quantity(2L)
                .price(product1.getPrice() * 2L)
                .build();
        ProdOrderDetail orderDetail2 = ProdOrderDetail.builder()
                .product(product2)
                .quantity(3L)
                .price(product2.getPrice() * 3L)
                .build();
        ProdOrderDetail orderDetail3 = ProdOrderDetail.builder()
                .product(product3)
                .quantity(4L)
                .price(product3.getPrice() * 4L)
                .build();

        ProdOrder order = ProdOrder.builder().build();

        given(orderCancelService.requestCancel(any(Long.class), any(List.class)))
                .willReturn(OrderCancelResponse.builder()
                        .refundInfoResponse(RefundInfoResponse.builder()
                                .deliveryFee(0)
                                .refundFee(0)
                                .totalPrice(20000L)
                                .discountPrice(0L)
                                .build()
                        )
                        .productResponses(List.of(
                                        ProductInfoResponse.of(orderDetail1),
                                        ProductInfoResponse.of(orderDetail2),
                                        ProductInfoResponse.of(orderDetail3)
                                )
                        )
                        .build()

                );

        // when // then
        mockMvc.perform(
                        get("/orders/cancel-flow")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("orderId", "1")
                                .param("productIds", "1", "2", "3")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-cancel-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("orderId")
                                        .description("주문 ID"),
                                parameterWithName("productIds")
                                        .description("취소 상품ID 리스트")
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
                                fieldWithPath("data.productResponses").type(JsonFieldType.ARRAY)
                                        .description("취소 상품 리스트"),
                                fieldWithPath("data.productResponses[].quantity").type(JsonFieldType.NUMBER)
                                        .description("취소 상품 수량"),
                                fieldWithPath("data.productResponses[].name").type(JsonFieldType.STRING)
                                        .description("취소 상품 이름"),
                                fieldWithPath("data.productResponses[].price").type(JsonFieldType.NUMBER)
                                        .description("취소 상품 가격"),
                                fieldWithPath("data.productResponses[].image").type(JsonFieldType.STRING)
                                        .description("취소 상품 썸네일"),
                                fieldWithPath("data.refundInfoResponse").type(JsonFieldType.OBJECT)
                                        .description("취소 상품 썸네일"),
                                fieldWithPath("data.refundInfoResponse.deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("환불 배송비"),
                                fieldWithPath("data.refundInfoResponse.refundFee").type(JsonFieldType.NUMBER)
                                        .description("환불 금액"),
                                fieldWithPath("data.refundInfoResponse.discountPrice").type(JsonFieldType.NUMBER)
                                        .description("할인 금액"),
                                fieldWithPath("data.refundInfoResponse.totalPrice").type(JsonFieldType.NUMBER)
                                        .description("상품 전체 금액")
                        )));
    }
}
