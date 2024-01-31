package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.order.domain.ProductValue;
import com.objects.marketbridge.order.service.dto.CreateOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderHttp {

    public static class Request {
        private Long amount;
        private Long addressId;
        private String orderName;
        private List<ProductValue> productValues;

        @Builder
        public Request(Long amount, Long addressId, String orderName, List<ProductValue> productValues) {
            this.amount = amount;
            this.addressId = addressId;
            this.orderName = orderName;
            this.productValues = productValues;
        }

        public CreateOrderDto toDto(String tid, Long memberId) {
            return CreateOrderDto.builder()
                    .memberId(memberId)
                    .tid(tid)
                    .addressId(addressId)
                    .orderName(orderName)
                    .totalOrderPrice(amount)
                    .productValues(productValues)
                    .build();
        }

        public KakaoPayReadyRequest toKakaoReadyRequest(String orderNo, Long memberId, String cid, String approvalUrl, String failUrl, String cancelUrl) {
            return KakaoPayReadyRequest.builder()
                    .cid(cid)
                    .approvalUrl(approvalUrl)
                    .failUrl(failUrl)
                    .cancelUrl(cancelUrl)
                    .quantity(productValues.stream().mapToLong(ProductValue::getQuantity).sum())
                    .partnerOrderId(orderNo)
                    .partnerUserId(memberId.toString())
                    .itemName(orderName)
                    .taxFreeAmount(0L)
                    .totalAmount(amount)
                    .build();
        }
    }

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


