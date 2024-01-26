package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.config.KakaoPayConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class KakaoPaymentApproveServiceTest {

    @Autowired KakaoPayConfig kakaoPayConfig;
    @Autowired OrderRepository orderRepository;

    @DisplayName("카카오페이 결제승인 테스트")
    @Test
    void execute(){
        //given

        //when

        //then
    }
}