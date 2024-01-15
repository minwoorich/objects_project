package com.objects.marketbridge.domain.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankCode {
    KAKAOBANK("90", "카카오뱅크"),
    TOSSBANK("92", "토스뱅크"),
    HANA("81", "하나은행"),
    KOOKMIN("06", "KB국민은행"),
    DAEGUBANK("31", "DGB대구은행"),
    KDBBANK("02", "KDB산업은행"),
    NONGHYEOP("11", "NH농협은행"),
    IBK("03", "IBK기업은행"),
    SHINHAN("88", "신한은행"),
    BUSANBANK("32", "부산은행"),
    GWANGJUBANK("34", "광주은행"),
    KYONGNAMBANK("39", "경남은행"),
    CITI("27","씨티은행"),
    WOORI("20", "우리은행"),
    POST("71", "우체국예금보험");

    private final String code;
    private final String name;
}
