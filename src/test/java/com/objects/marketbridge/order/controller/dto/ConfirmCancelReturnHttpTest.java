package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.ConfirmCancelReturnDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ConfirmCancelReturnHttpTest {

    @Test
    @DisplayName("Dto로 변환할 수 있다.")
    public void toDto() {
        // given
        ConfirmCancelReturnHttp.OrderDetailInfo httpOrderDetailInfo1 = ConfirmCancelReturnHttp.OrderDetailInfo.builder()
                .orderDetailId(1L)
                .numberOfCancellation(1L)
                .build();
        ConfirmCancelReturnHttp.OrderDetailInfo httpOrderDetailInfo2 = ConfirmCancelReturnHttp.OrderDetailInfo.builder()
                .orderDetailId(2L)
                .numberOfCancellation(2L)
                .build();
        List<ConfirmCancelReturnHttp.OrderDetailInfo> httpOrderDetailInfos = List.of(httpOrderDetailInfo1, httpOrderDetailInfo2);

        ConfirmCancelReturnHttp.Request request = ConfirmCancelReturnHttp.Request.builder()
                .orderDetailInfos(httpOrderDetailInfos)
                .cancelReason("단순변심")
                .build();


        // when
        ConfirmCancelReturnDto.Request result = request.toDto();

        // then
        assertThat(result.getOrderDetailInfos()).hasSize(2);
        assertThat(result.getOrderDetailInfos().get(0).getOrderDetailId()).isEqualTo(1L);
        assertThat(result.getOrderDetailInfos().get(0).getNumberOfCancellation()).isEqualTo(1L);
        assertThat(result.getOrderDetailInfos().get(1).getOrderDetailId()).isEqualTo(2L);
        assertThat(result.getOrderDetailInfos().get(1).getNumberOfCancellation()).isEqualTo(2L);

        assertThat(result.getCancelReason()).isEqualTo("단순변심");
    }

    @Test
    @DisplayName("Http.orderDetailInfo가 주어지면 dto.orderDetailInfo로 변환한다.")
    public void orderDetailInfo_of() {
        // given
        ConfirmCancelReturnHttp.OrderDetailInfo httpOrderDetailInfo = ConfirmCancelReturnHttp.OrderDetailInfo.builder()
                .orderDetailId(1L)
                .numberOfCancellation(1L)
                .build();

        // when
        ConfirmCancelReturnDto.OrderDetailInfo dtoOrderDetailInfo = ConfirmCancelReturnHttp.OrderDetailInfo.of(httpOrderDetailInfo);

        // then
        assertThat(dtoOrderDetailInfo.getOrderDetailId()).isEqualTo(1L);
        assertThat(dtoOrderDetailInfo.getNumberOfCancellation()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Dto.Response가 주어지면 Http.Response로 변환한다.")
    public void response_of() {
        // given
        LocalDateTime cancellationDate = LocalDateTime.of(2024, 2, 3, 3, 9);

        ConfirmCancelReturnDto.ProductInfo productInfo1 = ConfirmCancelReturnDto.ProductInfo.builder()
                .productId(1L)
                .productNo("1")
                .name("빵빵이키링")
                .price(1000L)
                .quantity(1L)
                .build();
        ConfirmCancelReturnDto.ProductInfo productInfo2 = ConfirmCancelReturnDto.ProductInfo.builder()
                .productId(2L)
                .productNo("2")
                .name("옥지얌키링")
                .price(2000L)
                .quantity(2L)
                .build();
        List<ConfirmCancelReturnDto.ProductInfo> productInfos = List.of(productInfo1, productInfo2);

        LocalDateTime refundDate = LocalDateTime.of(2024, 2, 3, 3, 10);
        ConfirmCancelReturnDto.RefundInfo refundInfo = ConfirmCancelReturnDto.RefundInfo.builder()
                .totalRefundAmount(5000L)
                .refundMethod("카드")
                .refundProcessedAt(refundDate)
                .build();

        ConfirmCancelReturnDto.Response param = ConfirmCancelReturnDto.Response.builder()
                .orderId(1L)
                .orderNo("1")
                .totalPrice(5000L)
                .cancellationDate(cancellationDate)
                .cancelledItems(productInfos)
                .refundInfo(refundInfo)
                .build();

        // when
        ConfirmCancelReturnHttp.Response result = ConfirmCancelReturnHttp.Response.of(param);

        // then
        assertThat(result.getOrderId()).isEqualTo(1L);
        assertThat(result.getOrderNo()).isEqualTo("1");
        assertThat(result.getTotalPrice()).isEqualTo(5000L);
        assertThat(result.getCancellationDate()).isEqualTo(cancellationDate);

        assertThat(result.getCancelledItems().size()).isEqualTo(2L);
        assertThat(result.getCancelledItems().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getCancelledItems().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getCancelledItems().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getCancelledItems().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getCancelledItems().get(0).getQuantity()).isEqualTo(1L);

        assertThat(result.getCancelledItems().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getCancelledItems().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getCancelledItems().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getCancelledItems().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getCancelledItems().get(1).getQuantity()).isEqualTo(2L);

        assertThat(result.getRefundInfo().getRefundMethod()).isEqualTo("카드");
        assertThat(result.getRefundInfo().getRefundProcessedAt()).isEqualTo(refundDate);
        assertThat(result.getRefundInfo().getTotalRefundAmount()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("Dto.ProductInfo가 주어지면 Http.ProductInfo를 반환한다.")
    public void productInfo_of() {
        // given
        ConfirmCancelReturnDto.ProductInfo dtoProductInfo = ConfirmCancelReturnDto.ProductInfo.builder()
                .productId(1L)
                .productNo("1")
                .name("빵빵이키링")
                .price(1000L)
                .quantity(1L)
                .build();

        // when
        ConfirmCancelReturnHttp.ProductInfo result = ConfirmCancelReturnHttp.ProductInfo.of(dtoProductInfo);

        // then
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getProductNo()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("빵빵이키링");
        assertThat(result.getPrice()).isEqualTo(1000L);
        assertThat(result.getQuantity()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Dto.RefundInfo가 주어지면 Http.RefundInfo를 반환한다.")
    public void refundInfo_of() {
        // given
        LocalDateTime refundDate = LocalDateTime.of(2024, 2, 3, 3, 10);
        ConfirmCancelReturnDto.RefundInfo dtoRefundInfo = ConfirmCancelReturnDto.RefundInfo.builder()
                .totalRefundAmount(5000L)
                .refundMethod("카드")
                .refundProcessedAt(refundDate)
                .build();

        // when
        ConfirmCancelReturnHttp.RefundInfo result = ConfirmCancelReturnHttp.RefundInfo.of(dtoRefundInfo);

        // then
        assertThat(result.getRefundMethod()).isEqualTo("카드");
        assertThat(result.getRefundProcessedAt()).isEqualTo(refundDate);
        assertThat(result.getTotalRefundAmount()).isEqualTo(5000L);
    }
}