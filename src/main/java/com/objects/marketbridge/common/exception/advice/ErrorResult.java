package com.objects.marketbridge.common.exception.advice;

import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResult {

    private ErrorCode code;
    private String message;

    // 서버 에러용 생성자
    public ErrorResult(String message) {
        this.message = message;
    }
}
