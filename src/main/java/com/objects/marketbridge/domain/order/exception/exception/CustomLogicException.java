package com.objects.marketbridge.domain.order.exception.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomLogicException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public CustomLogicException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
