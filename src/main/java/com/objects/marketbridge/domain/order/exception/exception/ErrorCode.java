package com.objects.marketbridge.domain.order.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PAYMENT_AMOUNT("유효하지 않은 액수"),
    SHIPPING_ADDRESS_NOT_REGISTERED("등록된 배송지 정보가 없습니다.");

    private final String message;
}
