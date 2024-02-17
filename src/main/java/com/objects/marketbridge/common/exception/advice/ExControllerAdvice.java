package com.objects.marketbridge.common.exception.advice;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice  {

    // 비즈니스 로직 예외
    @ExceptionHandler(value = CustomLogicException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse<ErrorResult> customExHandler(CustomLogicException e, HttpServletRequest httpRequest) {
        log.error("[exceptionHandler] {} ", e.getMessage());
        log.error("[exceptionHandler] {} ", Arrays.toString(e.getStackTrace()));

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(e.getErrorCode())
                .timestamp(e.getTimestamp().toString())
                .message(e.getMessage())
                .build();

        return ApiErrorResponse.of(e.getHttpStatus().value(), e.getHttpStatus(), httpRequest.getRequestURI(), errorResult);
    }

    // @Valid 에러 주로 유효하지 않은 범위의 값을 입력할 경우 발생
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse<ErrorResult> handleMethodArgumentNotValidExHandler(Exception e, HttpRequest httpRequest) {
        log.error("[exceptionHandler] {} ", e.getMessage());
        log.error("[exceptionHandler] {} ", Arrays.toString(e.getStackTrace()));

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(ErrorCode.INVALID_INPUT_VALUE)
                .timestamp(LocalDateTime.now().toString())
                .message("유효하지 않은 입력값입니다")
                .build();

        return ApiErrorResponse.of(BAD_REQUEST.value(), BAD_REQUEST, httpRequest.getURI().toString(), errorResult);
    }

    // 지원하지 않는 HTTP 메서드 호출시 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiErrorResponse<ErrorResult> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpRequest httpRequest) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(ErrorCode.METHOD_NOT_ALLOWED)
                .timestamp(LocalDateTime.now().toString())
                .message("지원하지 않는 HTTP 메서드를 호출하였습니다")
                .build();
        return ApiErrorResponse.of(BAD_REQUEST.value(), BAD_REQUEST, httpRequest.getURI().toString(), errorResult);
    }

    // 서버에러
    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiErrorResponse<String> serverExHandler(Exception e) {
        log.error("[exceptionHandler] ex : {}", e.getMessage());
        log.error("[exceptionHandler] {} ", e);
        return ApiErrorResponse.of(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, null, null);
    }
}
