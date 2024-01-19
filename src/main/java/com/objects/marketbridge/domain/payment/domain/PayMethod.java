package com.objects.marketbridge.domain.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayMethod {
    CARD("카드결제"), TRANSFER("계좌이체"), VIRTUAL("가상계좌");

    private final String name;
}
