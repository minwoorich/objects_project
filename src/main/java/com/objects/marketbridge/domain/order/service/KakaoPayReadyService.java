package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.order.controller.response.KakaoPayReadyResponse;
import com.objects.marketbridge.domain.order.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.domain.payment.config.KakaoPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.objects.marketbridge.domain.payment.config.KakaoPayConfig.KAKAO_BASE_URL;

@Service
@RequiredArgsConstructor
public class KakaoPayReadyService {

    private final KakaoPayConfig kakaoPayConfig;
    public KakaoPayReadyResponse execute(KakaoPayReadyRequest request) {
        RestClient restClient = RestClient.builder()
                .baseUrl(KAKAO_BASE_URL)
                .defaultHeader("Authorization", "KaKaoAK "+ kakaoPayConfig.getAdminKey())
                .build();

        return restClient.post()
                .uri("/ready")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(request)
                .retrieve()
                .body(KakaoPayReadyResponse.class);
    }
}
