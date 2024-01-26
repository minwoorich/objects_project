package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.payment.config.KakaoPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class KakaoPayReadyServiceTest {

    @Autowired KakaoPayConfig kakaoPayConfig;



}