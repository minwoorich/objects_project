package com.objects.marketbridge.common.exception.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomLogicException extends RuntimeException {

    private ErrorCode errorCode;

    public CustomLogicException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public CustomLogicException(String message) {
        super(message);
    }
}
