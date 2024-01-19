package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.RestDocsSupport;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class OrderControllerRestDocsTest extends RestDocsSupport {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final TossPaymentConfig tossPaymentConfig = mock(TossPaymentConfig.class);
    private final CreateOrderService createOrderService = mock(CreateOrderService.class);
    @Override
    protected Object initController() {
        return new OrderController(memberRepository, tossPaymentConfig, createOrderService);
    }
    
//    @DisplayName("")
//    @Test
//    void <unnamed>(){
//        //given
//
//        //when
//
//        //then
//    }
}
