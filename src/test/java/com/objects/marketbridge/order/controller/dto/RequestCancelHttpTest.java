//package com.objects.marketbridge.order.controller.dto;
//
//import com.objects.marketbridge.order.service.dto.RequestCancelDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class RequestCancelHttpTest {
//
//    @Test
//    @DisplayName("Dto.Response 주어지면 Http.Response 반환한다.")
//    public void response_of() {
//        LocalDateTime orderDate = LocalDateTime.of(2024, 2, 3, 3, 9);
//        LocalDateTime cancelDate = LocalDateTime.of(2024, 2, 3, 4, 4);
//
//        RequestCancelDto.ProductInfo dtoProductInfo1 = RequestCancelDto.ProductInfo.builder()
//                .name("빵빵이키링")
//                .price(1000L)
//                .quantity(1L)
//                .image("빵빵이썸네일")
//                .build();
//        RequestCancelDto.ProductInfo dtoProductInfo2 = RequestCancelDto.ProductInfo.builder()
//                .name("옥지얌키링")
//                .price(2000L)
//                .quantity(2L)
//                .image("옥지얌썸네일")
//                .build();
//        List<RequestCancelDto.ProductInfo> dtoProductInfos = List.of(dtoProductInfo1, dtoProductInfo2);
//
//        RequestCancelDto.CancelRefundInfo dtoCancelRefundInfo = RequestCancelDto.CancelRefundInfo.builder()
//                .deliveryFee(0L)
//                .refundFee(0L)
//                .discountPrice(3000L)
//                .totalPrice(5000L)
//                .build();
//
//        RequestCancelDto.Response dtoResponse = RequestCancelDto.Response.builder()
//                .productInfos(dtoProductInfos)
//                .cancelRefundInfo(dtoCancelRefundInfo)
//                .build();
//
//        // when
//        RequestCancelHttp.Response result = RequestCancelHttp.Response.of(dtoResponse);
//
//        // then
//        assertThat(result.getProductInfos().size()).isEqualTo(2L);
//        assertThat(result.getProductInfos().get(0).getName()).isEqualTo("빵빵이키링");
//        assertThat(result.getProductInfos().get(0).getPrice()).isEqualTo(1000L);
//        assertThat(result.getProductInfos().get(0).getQuantity()).isEqualTo(1L);
//        assertThat(result.getProductInfos().get(0).getImage()).isEqualTo("빵빵이썸네일");
//
//        assertThat(result.getProductInfos().get(1).getName()).isEqualTo("옥지얌키링");
//        assertThat(result.getProductInfos().get(1).getPrice()).isEqualTo(2000L);
//        assertThat(result.getProductInfos().get(1).getQuantity()).isEqualTo(2L);
//        assertThat(result.getProductInfos().get(1).getImage()).isEqualTo("옥지얌썸네일");
//
//        assertThat(result.getCancelRefundInfo().getDeliveryFee()).isEqualTo(0L);
//        assertThat(result.getCancelRefundInfo().getRefundFee()).isEqualTo(0L);
//        assertThat(result.getCancelRefundInfo().getDiscountPrice()).isEqualTo(3000L);
//        assertThat(result.getCancelRefundInfo().getTotalPrice()).isEqualTo(5000L);
//    }
//
//    @Test
//    @DisplayName("Dto.ProductInfo 주어지면 Http.ProductInfo 반환한다.")
//    public void productInfo_of() {
//        // given
//        RequestCancelDto.ProductInfo dtoProductInfo = RequestCancelDto.ProductInfo.builder()
//                .name("빵빵이키링")
//                .price(1000L)
//                .quantity(1L)
//                .image("빵빵이썸네일")
//                .build();
//
//        // when
//        RequestCancelHttp.ProductInfo result = RequestCancelHttp.ProductInfo.of(dtoProductInfo);
//
//        // then
//        assertThat(result.getName()).isEqualTo("빵빵이키링");
//        assertThat(result.getPrice()).isEqualTo(1000L);
//        assertThat(result.getQuantity()).isEqualTo(1L);
//        assertThat(result.getImage()).isEqualTo("빵빵이썸네일");
//    }
//
//    @Test
//    @DisplayName("Dto.CancelRefundInfo가 주어지면 Http.CancelRefundInfo로 반환한다.")
//    public void cancelRefundInfo_of() {
//        RequestCancelDto.CancelRefundInfo dtoCancelRefundInfo = RequestCancelDto.CancelRefundInfo.builder()
//                .deliveryFee(0L)
//                .refundFee(0L)
//                .discountPrice(3000L)
//                .totalPrice(5000L)
//                .build();
//
//        // when
//        RequestCancelHttp.CancelRefundInfo result = RequestCancelHttp.CancelRefundInfo.of(dtoCancelRefundInfo);
//
//        // then
//        assertThat(result.getDeliveryFee()).isEqualTo(0L);
//        assertThat(result.getRefundFee()).isEqualTo(0L);
//        assertThat(result.getDiscountPrice()).isEqualTo(3000L);
//        assertThat(result.getTotalPrice()).isEqualTo(5000L);
//    }
//}