package com.objects.marketbridge.common.infra;

import com.objects.marketbridge.common.dto.KakaoPayApproveRequest;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static com.objects.marketbridge.common.config.KakaoPayConfig.*;
import static com.objects.marketbridge.common.security.constants.SecurityConst.AUTHORIZATION;
import static io.jsonwebtoken.Header.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    public KakaoPayReadyResponse ready(KakaoPayReadyRequest request) {

        MultiValueMap<String, String> requestMap = request.toMultiValueMap();

        RestClient restClient = setup();

        return restClient.post()
                .uri(READY_END_POINT)
                .body(requestMap)
                .retrieve()
                .body(KakaoPayReadyResponse.class);
    }


    //인터페이스 -> 정기구독 정기용, 단건용
    public KakaoPayApproveResponse approve(KakaoPayApproveRequest request) {

        MultiValueMap<String, String> requestMap = request.toMultiValueMap();

        RestClient restClient = setup();

        return restClient.post()
                .uri("/approve")
                .body(requestMap)
                .retrieve()
                .body(KakaoPayApproveResponse.class);
    }

    // 취소

    // 정기구독 비활성화

    // 정기결제 상태조회

    // 주문조회

    private RestClient setup() {
        return RestClient.builder()
                .baseUrl(KAKAO_BASE_URL)
                .messageConverters((converters) ->
                        converters.add(new FormHttpMessageConverter()))
                .defaultHeaders((httpHeaders -> {
                    httpHeaders.add(AUTHORIZATION, KAKAO_AK + ADMIN_KEY);
                    httpHeaders.add(ACCEPT, APPLICATION_JSON.toString());
                    httpHeaders.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED + ";charset=UTF-8");
                }))
                .build();
    }
}