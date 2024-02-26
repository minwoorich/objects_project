package com.objects.marketbridge.domains.order.controller.dto;

import com.objects.marketbridge.common.kakao.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.kakao.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import com.objects.marketbridge.domains.order.domain.ProductValue;
import com.objects.marketbridge.domains.order.service.dto.CreateOrderDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateOrderHttp {

    // Request 클래스
    @Getter
    @NoArgsConstructor
    public static class Request {

        @NotNull
        private Long totalAmount;

        @NotNull
        private Long totalDiscountAmount; // 쿠폰 할인 만 적용

        @NotNull
        private Long realAmount;

        @NotNull
        private Long addressId;

        @NotNull
        private String orderName;

        @NotNull
        private List<ProductValue> productValues;

        @Builder
        public Request(Long totalAmount, Long totalDiscountAmount, Long realAmount, Long addressId, String orderName, List<ProductValue> productValues) {
            validAmount(totalAmount, totalDiscountAmount, realAmount);
            this.totalAmount = totalAmount;
            this.totalDiscountAmount = totalDiscountAmount;
            this.realAmount = realAmount;
            this.addressId = addressId;
            this.orderName = orderName;
            this.productValues = productValues;
        }

        private void validAmount(Long totalAmount, Long totalDiscountAmount, Long realAmount) {
            if (totalAmount != totalDiscountAmount + realAmount) {
                throw CustomLogicException.createBadRequestError(ErrorCode.INVALID_INPUT_VALUE, "금액 계산이 맞지 않습니다", LocalDateTime.now());
            }
        }

        public CreateOrderDto toDto(String orderNo, String tid, Long memberId) {
            return CreateOrderDto.builder()
                    .orderNo(orderNo)
                    .memberId(memberId)
                    .tid(tid)
                    .addressId(addressId)
                    .orderName(orderName)
                    .totalOrderPrice(totalAmount)
                    .realOrderPrice(realAmount)
                    .totalDiscountPrice(totalDiscountAmount)
                    .productValues(productValues)
                    .build();
        }

        public KakaoPayReadyRequest toKakaoReadyRequest(String orderNo, Long memberId, String cid, String approvalUrl, String failUrl, String cancelUrl) {
            return KakaoPayReadyRequest.builder()
                    .partnerOrderId(orderNo)
                    .partnerUserId(memberId.toString())
                    .cid(cid)
                    .approvalUrl(approvalUrl)
                    .failUrl(failUrl)
                    .cancelUrl(cancelUrl)
                    .quantity(productValues.stream().mapToLong(ProductValue::getQuantity).sum())
                    .itemName(orderName)
                    .taxFreeAmount(0L)
                    .totalAmount(realAmount)
                    .build();
        }
    }

    // Response 클래스
    @Getter
    @NoArgsConstructor
    public static class Response{

        private String tid; // 결제 고유 번호
        private String nextRedirectPcUrl; // 요청한 클라이언트가 PC 웹일 경우
        private String nextRedirectAppUrl;// 요청한 클라이언트가 모바일 앱일 경우
        private String nextRedirectMobileUrl;// 요청한 클라이언트가 모바일 웹일 경우
        private String androidAppScheme; //카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme)
        private String iosAppScheme; // 카카오페이 결제 화면으로 이동하는 iOS 앱 스킴
        private String createdAt;

        @Builder
        private Response(String tid, String nextRedirectPcUrl, String nextRedirectAppUrl, String nextRedirectMobileUrl, String androidAppScheme, String iosAppScheme, String createdAt) {
            this.tid = tid;
            this.nextRedirectPcUrl = nextRedirectPcUrl;
            this.nextRedirectAppUrl = nextRedirectAppUrl;
            this.nextRedirectMobileUrl = nextRedirectMobileUrl;
            this.androidAppScheme = androidAppScheme;
            this.iosAppScheme = iosAppScheme;
            this.createdAt = createdAt;
        }

        public static Response of(KakaoPayReadyResponse kakaoResp) {

            return Response.builder()
                    .tid(kakaoResp.getTid())
                    .nextRedirectPcUrl(kakaoResp.getNextRedirectPcUrl())
                    .nextRedirectAppUrl(kakaoResp.getNextRedirectAppUrl())
                    .nextRedirectMobileUrl(kakaoResp.getNextRedirectMobileUrl())
                    .androidAppScheme(kakaoResp.getAndroidAppScheme())
                    .iosAppScheme(kakaoResp.getIosAppScheme())
                    .createdAt(kakaoResp.getCreatedAt())
                    .build();
        }

    }
}


