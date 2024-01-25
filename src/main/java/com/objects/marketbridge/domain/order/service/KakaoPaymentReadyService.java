package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.order.controller.response.KakaoPaymentReadyResponse;
import com.objects.marketbridge.domain.order.dto.KakaoPaymentReadyRequest;
import com.objects.marketbridge.domain.payment.config.KakaoPaymentConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.objects.marketbridge.domain.payment.config.KakaoPaymentConfig.KAKAO_BASE_URL;

@Service
@RequiredArgsConstructor
public class KakaoPaymentReadyService {

    private final KakaoPaymentConfig kakaoPaymentConfig;
    public KakaoPaymentReadyResponse execute(KakaoPaymentReadyRequest request) {
        RestClient restClient = RestClient.builder()
                .baseUrl(KAKAO_BASE_URL)
                .defaultHeader("Authorization", "KaKaoAK "+kakaoPaymentConfig.getAdminKey())
                .build();

        return restClient.post()
                .uri("/ready")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(request)
                .retrieve()
                .body(KakaoPaymentReadyResponse.class);
    }
}
