package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.GetCancelReturnDetailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetCancelReturnDetailHttpTest {

    @Test
    @DisplayName("Dto.Response 주어지면 Http.Response 반환한다.")
    public void response_of() {
        // given
        LocalDateTime orderDate = LocalDateTime.of(2024, 2, 3, 3, 9);
        LocalDateTime cancelDate = LocalDateTime.of(2024, 2, 3, 4, 4);

        GetCancelReturnDetailDto.ProductInfo dtoProductInfo1 = GetCancelReturnDetailDto.ProductInfo.builder()
                .productId(1L)
                .productNo("1")
                .name("빵빵이키링")
                .price(1000L)
                .quantity(1L)
                .build();
        GetCancelReturnDetailDto.ProductInfo dtoProductInfo2 = GetCancelReturnDetailDto.ProductInfo.builder()
                .productId(2L)
                .productNo("2")
                .name("옥지얌키링")
                .price(2000L)
                .quantity(2L)
                .build();
        List<GetCancelReturnDetailDto.ProductInfo> dtoProductInfos = List.of(dtoProductInfo1, dtoProductInfo2);

        GetCancelReturnDetailDto.CancelRefundInfo dtoCancelRefundInfo = GetCancelReturnDetailDto.CancelRefundInfo.builder()
                .deliveryFee(0L)
                .refundFee(0L)
                .discountPrice(3000L)
                .totalPrice(5000L)
                .build();

        GetCancelReturnDetailDto.Response dtoResponse = GetCancelReturnDetailDto.Response.builder()
                .orderDate(orderDate)
                .cancelDate(cancelDate)
                .orderNo("1")
                .cancelReason("단순변심")
                .productInfos(dtoProductInfos)
                .cancelRefundInfo(dtoCancelRefundInfo)
                .build();

        // when
        GetCancelReturnDetailHttp.Response result = GetCancelReturnDetailHttp.Response.of(dtoResponse);

        // then
        assertThat(result.getOrderDate()).isEqualTo(orderDate);
        assertThat(result.getCancelDate()).isEqualTo(cancelDate);
        assertThat(result.getOrderNo()).isEqualTo("1");
        assertThat(result.getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getProductInfos().size()).isEqualTo(2L);
        assertThat(result.getProductInfos().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getProductInfos().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getProductInfos().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getProductInfos().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getProductInfos().get(0).getQuantity()).isEqualTo(1L);

        assertThat(result.getProductInfos().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getProductInfos().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getProductInfos().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getProductInfos().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getProductInfos().get(1).getQuantity()).isEqualTo(2L);

        assertThat(result.getCancelRefundInfo().getDeliveryFee()).isEqualTo(0L);
        assertThat(result.getCancelRefundInfo().getRefundFee()).isEqualTo(0L);
        assertThat(result.getCancelRefundInfo().getDiscountPrice()).isEqualTo(3000L);
        assertThat(result.getCancelRefundInfo().getTotalPrice()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("Dto.ProductInfo 주어지면 Http.ProductInfo 반환한다.")
    public void productInfo_of() {
        // given
        GetCancelReturnDetailDto.ProductInfo dtoProductInfo = GetCancelReturnDetailDto.ProductInfo.builder()
                .productId(1L)
                .productNo("1")
                .name("빵빵이키링")
                .price(1000L)
                .quantity(1L)
                .build();

        // when
        GetCancelReturnDetailHttp.ProductInfo result = GetCancelReturnDetailHttp.ProductInfo.of(dtoProductInfo);

        // then
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getProductNo()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("빵빵이키링");
        assertThat(result.getPrice()).isEqualTo(1000L);
        assertThat(result.getQuantity()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Dto.CancelRefundInfo가 주어지면 Http.CancelRefundInfo로 반환한다.")
    public void cancelRefundInfo_of() {
        // given
        GetCancelReturnDetailDto.CancelRefundInfo dtoCancelRefundInfo = GetCancelReturnDetailDto.CancelRefundInfo.builder()
                .deliveryFee(0L)
                .refundFee(0L)
                .discountPrice(3000L)
                .totalPrice(5000L)
                .build();

        // when
        GetCancelReturnDetailHttp.CancelRefundInfo result = GetCancelReturnDetailHttp.CancelRefundInfo.of(dtoCancelRefundInfo);

        // then
        assertThat(result.getDeliveryFee()).isEqualTo(0L);
        assertThat(result.getRefundFee()).isEqualTo(0L);
        assertThat(result.getDiscountPrice()).isEqualTo(3000L);
        assertThat(result.getTotalPrice()).isEqualTo(5000L);
    }
}