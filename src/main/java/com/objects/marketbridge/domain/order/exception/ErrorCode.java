package com.objects.marketbridge.domain.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PAYMENT_AMOUNT("유효하지 않은 액수");

    private final String message;
}
