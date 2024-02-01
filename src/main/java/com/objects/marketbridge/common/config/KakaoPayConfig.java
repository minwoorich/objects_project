package com.objects.marketbridge.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KakaoPayConfig {

    public static final String KAKAO_BASE_URL = "https://kapi.kakao.com/v1/payment";

    public static final String READY_END_POINT = "/ready";

    public static final String APPROVE_END_POINT = "/approve";

    public static final String SUBS_END_POINT = "/subscribe";

    public static final String CANCEL_END_POINT = "/cancel";

    public static final String ONE_TIME_CID = "TC0ONETIME";

    public static final String SUBS_CID = "TCSUBSCRIP";

    public static final String KAKAO_AK = "KakaoAK ";


    @Value("${payment.kakao.admin_key}")
    private String adminKey;

    @Value("${payment.kakao.test_api_key}")
    private String testApiKey;

    @Value("${host}")
    private String host;


    public String getRedirectCancelUrl() {
        return host + "/kakao-pay/cancel";
    }

    public String getRedirectFailUrl() {
        return host + "/kakao-pay/fail";
    }

    public String createApprovalUrl(String uri) {
        return host + uri + "/kakao-pay/approval";
    }


}
