package com.objects.marketbridge.domain.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardCode {
    KAKAOBANK("15", "카카오뱅크"),
    KBANK("3A", "케이뱅크"),
    TOSSBANK("24", "토스뱅크"),
    KDBBANK("30", "산업"),
    HANA("21", "하나"),
    HYUNDAI("61", "현대"),
    KOOKMIN("11", "국민"),
    NONGHYEOP("91", "농협"),
    SHINHAN("41", "신한"),
    CITI("36", "씨티"),
    POST("37", "우체국"),
    LOTTE("71", "롯데");

    private final String code;
    private final String name;
}
