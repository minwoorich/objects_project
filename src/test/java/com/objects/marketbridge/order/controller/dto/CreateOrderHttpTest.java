package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.order.domain.ProductValue;
import com.objects.marketbridge.order.service.dto.CreateOrderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateOrderHttpTest {

    @DisplayName("CreateOrderHttp.Request 를 CreateOrderDto 로 변환할 수 있다")
    @Test
    void requestToDto() {

        // given
        CreateOrderHttp.Request request = CreateOrderHttp.Request.builder()
                .amount(5000L)
                .addressId(1L)
                .orderName("가방외 2건")
                .productValues(createProductValues())
                .build();

        String orderNo = "aaaa-aaaa-aaaa";
        String tid = "tid";
        Long memberId = 1L;

        // when
        CreateOrderDto dto = request.toDto(orderNo, tid, memberId);

        //then
        assertThat(dto).extracting(
                "tid",
                "memberId",
                "addressId",
                "orderName",
                "orderNo",
                "totalOrderPrice",
                "productValues")
                .containsExactlyInAnyOrder(
                        "tid",
                1L,
                1L,
                "가방외 2건",
                "aaaa-aaaa-aaaa",
                5000L,
                request.getProductValues()
        );
    }

    @DisplayName("CreateOrderHttp.Request 를 KakaoPayReadyRequest 로 변환할 수 있다")
    @Test
    void requestToKakaoPayReadyRequest() {

        // given
        CreateOrderHttp.Request request = CreateOrderHttp.Request.builder()
                .amount(5000L)
                .addressId(1L)
                .orderName("가방외 2건")
                .productValues(createProductValues())
                .build();

        String orderNo = "aaaa-aaaa-aaaa";
        Long memberId = 1L;
        String cid = "cid";
        String approvalUrl = "approvalUrl";
        String failUrl = "failUrl";
        String cancelUrl = "cancelUrl";

        // when
        KakaoPayReadyRequest kakaoReadyRequest = request.toKakaoReadyRequest(orderNo, memberId, cid, approvalUrl, failUrl, cancelUrl);

        //then
        assertThat(kakaoReadyRequest).extracting(
                        "partnerOrderId",
                        "partnerUserId",
                        "cid",
                        "approvalUrl",
                        "failUrl",
                        "cancelUrl",
                        "quantity",
                        "itemName",
                        "taxFreeAmount",
                        "totalAmount")
                .containsExactlyInAnyOrder(
                        orderNo,
                        memberId.toString(),
                        cid,
                        approvalUrl+"/"+orderNo,
                        failUrl+"/"+orderNo,
                        cancelUrl+"/"+orderNo,
                        request.getProductValues().stream().mapToLong(ProductValue::getQuantity).sum(),
                        request.getOrderName(),
                        0L,
                        request.getAmount()
                );

    }

    @DisplayName("KakaoPayReadyResponse 로 부터 CreateOrderHttp.Response 를 만들 수 있다.")
    @Test
    void response_of() {
        // given
        KakaoPayReadyResponse kakaoResp = KakaoPayReadyResponse.builder()
                .tid("tid")
                .nextRedirectPcUrl("pc")
                .nextRedirectAppUrl("app")
                .nextRedirectMobileUrl("mobile")
                .androidAppScheme("android")
                .iosAppScheme("app")
                .createdAt("createdAt")
                .build();

        // when
        CreateOrderHttp.Response createOrderResp = CreateOrderHttp.Response.of(kakaoResp);

        //then
        assertThat(createOrderResp).extracting(
                "tid",
                "nextRedirectPcUrl",
                "nextRedirectAppUrl",
                "nextRedirectMobileUrl",
                "androidAppScheme",
                "iosAppScheme",
                "createdAt").containsExactlyInAnyOrder(
                kakaoResp.getTid(),
                kakaoResp.getNextRedirectPcUrl(),
                kakaoResp.getNextRedirectAppUrl(),
                kakaoResp.getNextRedirectMobileUrl(),
                kakaoResp.getAndroidAppScheme(),
                kakaoResp.getIosAppScheme(),
                kakaoResp.getCreatedAt());
    }

    private List<ProductValue> createProductValues(){
        ProductValue productValue1 = ProductValue.builder()
                .sellerId(1L)
                .productId(1L)
                .couponId(1L)
                .quantity(1L)
                .deliveredDate("2023-03-31").build();

        ProductValue productValue2 = ProductValue.builder()
                .sellerId(2L)
                .productId(2L)
                .couponId(2L)
                .quantity(1L)
                .deliveredDate("2023-03-31").build();

        ProductValue productValue3 = ProductValue.builder()
                .sellerId(3L)
                .productId(3L)
                .couponId(3L)
                .quantity(1L)
                .deliveredDate("2023-03-31").build();

        return List.of(productValue1, productValue2, productValue3);
    }

}