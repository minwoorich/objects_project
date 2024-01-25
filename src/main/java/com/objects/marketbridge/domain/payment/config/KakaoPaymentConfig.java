package com.objects.marketbridge.domain.payment.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KakaoPaymentConfig {

    public static final String KAKAO_BASE_URL = "https://kapi.kakao.com/v1/payment";

    @Value("${payment.kakao.admin_key}")
    private String adminKey;

    @Value("${payment.kakao.test_api_key}")
    private String testApiKey;

    @Value("${payment.kakao.approval_url}")
    private String approvalUrl;

    @Value("${payment.kakao.cancel_url}")
    private String cancelUrl;

    @Value("${payment.kakao.fail_url}")
    private String failUrl;

    @Value("${payment.kakao.one_time.cid}")
    private String cid;
}
