package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.order.controller.response.KakaoPaymentReadyResponse;
import com.objects.marketbridge.domain.order.dto.KakaoPaymentReadyRequest;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.config.KakaoPaymentConfig;
import com.objects.marketbridge.domain.payment.controller.request.KakaoPayApproveRequest;
import com.objects.marketbridge.domain.payment.controller.response.KakaoPayApproveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.objects.marketbridge.domain.payment.config.KakaoPaymentConfig.KAKAO_BASE_URL;

@Service
@RequiredArgsConstructor
public class KakaoPaymentApproveService {

    private final KakaoPaymentConfig kakaoPaymentConfig;
    private final OrderRepository orderRepository;
    public KakaoPayApproveResponse execute(String pgToken, Long memberId, String tid, String cid) {

        Order order = orderRepository.findByTid(tid);
        KakaoPayApproveRequest request = createRequest(order, pgToken, memberId, tid, cid);

        RestClient restClient = RestClient.builder()
                .baseUrl(KAKAO_BASE_URL)
                .defaultHeader("Authorization", "KaKaoAK "+kakaoPaymentConfig.getAdminKey())
                .build();

        return restClient.post()
                .uri("/approve")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(request)
                .retrieve()
                .body(KakaoPayApproveResponse.class);
    }

    private KakaoPayApproveRequest createRequest(Order order, String pgToken, Long memberId, String tid, String cid) {

        return KakaoPayApproveRequest.builder()
                .tid(tid)
                .cid(cid)
                .partnerOrderId(order.getOrderNo())
                .partnerUserId(memberId.toString())
                .pgToken(pgToken)
                .build();
    }
}
