package com.objects.marketbridge.domain.order.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomLogicException extends RuntimeException {

    private ErrorCode errorCode;

    public CustomLogicException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
