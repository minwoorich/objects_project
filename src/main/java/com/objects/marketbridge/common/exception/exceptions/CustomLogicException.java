package com.objects.marketbridge.common.exception.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CustomLogicException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode errorCode;
    private LocalDateTime timestamp;

    @Builder
    private CustomLogicException(String message, ErrorCode errorCode,HttpStatus httpStatus, LocalDateTime timestamp) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public CustomLogicException(String message) {
        super(message);
    }
}
