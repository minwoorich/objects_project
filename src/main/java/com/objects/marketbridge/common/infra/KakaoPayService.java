package com.objects.marketbridge.common.infra;


import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;


import static com.objects.marketbridge.common.config.KakaoPayConfig.*;
import static com.objects.marketbridge.common.security.constants.SecurityConst.AUTHORIZATION;
import static io.jsonwebtoken.Header.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final KakaoPayConfig kakaoPayConfig;

    public KakaoPayReadyResponse ready(KakaoPayReadyRequest request) {

//        MultiValueMap<String, String> requestMap = request.toMultiValueMap();

        RestClient restClient = setup();

        return restClient.post()
                .uri(READY_END_POINT)
                .body(request)
                .retrieve()
                .body(KakaoPayReadyResponse.class);
    }


    //정기구독 1회차 , 단건결제
    public KakaoPayApproveResponse approve(KakaoPayApproveRequest request) {

//        MultiValueMap<String, String> requestMap = request.toMultiValueMap();

        RestClient restClient = setup();

        return restClient.post()
                .uri(APPROVE_END_POINT)
                .body(request)
                .retrieve()
                .body(KakaoPayApproveResponse.class);
    }

    //정기구독 2회차
    public KakaoPayApproveResponse subsApprove(KakaoPaySubsApproveRequest request) {

//        MultiValueMap<String, String> requestMap = request.toMultiValueMap();

        RestClient restClient = setup();

        return restClient.post()
                .uri(SUBS_END_POINT)
                .body(request)
                .retrieve()
                .body(KakaoPayApproveResponse.class);
    }

    // 취소
    public KaKaoCancelResponse cancel(String tid, Integer cancelAmount) {

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("cid", ONE_TIME_CID);
        requestMap.add("tid", tid);
        requestMap.add("cancel_amount", String.valueOf(cancelAmount));
        requestMap.add("cancel_tax_free_amount", "0");

        RestClient restClient = setup();

        return restClient.post()
                .uri(CANCEL_END_POINT)
                .body(requestMap)
                .retrieve()
                .body(KaKaoCancelResponse.class);
    }

    // 정기구독 비활성화

    // 정기결제 상태조회

    // 주문조회
    public KakaoPayOrderResponse getOrders(KakaoPayOrderRequest request) {

        RestClient restClient = setup();

        return restClient.post()
                .uri(ORDER_END_POINT)
                .body(request)
                .retrieve()
                .body(KakaoPayOrderResponse.class);
    }



    private RestClient setup() {

        return RestClient.builder()
                .baseUrl(KAKAO_BASE_URL)
                .defaultHeaders((httpHeaders -> {
                    httpHeaders.add(AUTHORIZATION, AUTH_SCHEME+ kakaoPayConfig.getSecretKeyDev());
                    httpHeaders.add(ACCEPT, APPLICATION_JSON.toString()+";charset=UTF-8");
                    httpHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
                }))
                .build();
    }
}