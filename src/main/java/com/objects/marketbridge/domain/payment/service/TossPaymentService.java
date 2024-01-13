package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.domain.payment.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.payment.dto.TossParamsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.objects.marketbridge.domain.payment.config.TossPaymentConfig.TOSS_BASE_URL;

@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final WebClient webClient;
    private final TossPaymentConfig tossPaymentConfig;
    public TossPaymentsResponse requestPaymentAccept(Long memberId, String paymentKey, String orderNo, Long totalPrice) {

        String encodedAuthKey = new String(Base64.getEncoder().encode(tossPaymentConfig.getTestSecretKey().getBytes(StandardCharsets.UTF_8)));
        return webClient.mutate()
                .baseUrl(TOSS_BASE_URL)
                .defaultHeaders(httpHeaders ->
                        httpHeaders.setBasicAuth(encodedAuthKey))
                .build()
                .post()
                .uri("/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new TossParamsDto(paymentKey, orderNo, totalPrice))
                .retrieve()
                .bodyToMono(TossPaymentsResponse.class)// 여기까지만 하면 SpringFlux 의 Mono<T> 타입 객체를 반환함
                .flux()
                .toStream() // SpringMVC 로 개발중인 경우 이렇게 Stream API를 사용해
                .findFirst()// Optional<T> 로 반환 해야함.
                .orElseThrow(IllegalArgumentException::new);

    }
}
