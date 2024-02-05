package com.objects.marketbridge.common.exception.advice;

import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResult {

    private ErrorCode errorCode; // ex) INVALID_DATA
    private  String message; // ex) "잘못된 입력입니다"
    private  String timestamp; // "2024-02-01T16:19:41.767602"
//    private  String location;

    // 서버 에러용 생성자


    @Builder
    private ErrorResult(ErrorCode errorCode, String message, String timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }
}
