package com.objects.marketbridge.common.exception.advice;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.INVALID_INPUT_VALUE;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice  {

    // 비즈니스 로직 예외
    @ExceptionHandler(value = CustomLogicException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse<ErrorResult> customExHandler(CustomLogicException e, HttpServletRequest httpRequest) {

        myPrintStackTrace(e.getMessage(), e.getStackTrace());
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(e.getErrorCode())
                .timestamp(e.getTimestamp().toString())
                .message(e.getMessage())
                .build();

        return ApiErrorResponse.of(e.getHttpStatus().value(), e.getHttpStatus(), httpRequest.getRequestURI(), errorResult);
    }

    // @Valid, @Validated 에러 주로 유효하지 않은 범위의 값을 입력할 경우 발생
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse<ErrorResult> handleMethodArgumentNotValidExHandler(MethodArgumentNotValidException e, HttpServletRequest httpRequest) {

        myPrintStackTrace(e.getMessage(), e.getStackTrace());
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.isEmpty() ? INVALID_INPUT_VALUE.getMessage() : fieldErrors.get(0).getDefaultMessage();

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(INVALID_INPUT_VALUE)
                .timestamp(LocalDateTime.now().toString())
                .message(errorMessage)
                .build();

        return ApiErrorResponse.of(BAD_REQUEST.value(), BAD_REQUEST, httpRequest.getRequestURI(), errorResult);
    }

    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse<ErrorResult> handleBindExHandler(BindException e, HttpServletRequest httpRequest) {

        myPrintStackTrace(e.getMessage(), e.getStackTrace());

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.isEmpty() ? INVALID_INPUT_VALUE.getMessage() : fieldErrors.get(0).getDefaultMessage();

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(INVALID_INPUT_VALUE)
                .timestamp(LocalDateTime.now().toString())
                .message(errorMessage)
                .build();

        return ApiErrorResponse.of(BAD_REQUEST.value(), BAD_REQUEST, httpRequest.getRequestURI(), errorResult);
    }

    // 지원하지 않는 HTTP 메서드 호출시 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiErrorResponse<ErrorResult> handleHttpRequestMethodNotSupportedExHandler(HttpRequestMethodNotSupportedException e, HttpServletRequest httpRequest) {

        myPrintStackTrace(e.getMessage(), e.getStackTrace());
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(ErrorCode.METHOD_NOT_ALLOWED)
                .timestamp(LocalDateTime.now().toString())
                .message(e.getMessage())
                .build();
        return ApiErrorResponse.of(BAD_REQUEST.value(), BAD_REQUEST, httpRequest.getRequestURI(), errorResult);
    }

    // 파라미터의 타입이 요청의 값과 호환되지 않는 경우. ex) /test/{1} -> /test/{a}
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiErrorResponse<ErrorResult> handleMethodArgumentTypeMismatchExHandler(MethodArgumentTypeMismatchException e, HttpServletRequest httpRequest) {

        myPrintStackTrace(e.getMessage(), e.getStackTrace());
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(INVALID_INPUT_VALUE)
                .timestamp(LocalDateTime.now().toString())
                .message(e.getMessage())
                .build();

        return ApiErrorResponse.of(BAD_REQUEST.value(), BAD_REQUEST, httpRequest.getRequestURI(), errorResult);
    }

    // URI 잘못 요청 했을때 -> ex) /cart -> /art
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiErrorResponse<ErrorResult> handleNoResourceFoundExHandler(NoResourceFoundException e, HttpServletRequest httpRequest) {

        myPrintStackTrace(e.getMessage(), e.getStackTrace());
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(INVALID_INPUT_VALUE)
                .timestamp(LocalDateTime.now().toString())
                .message(e.getMessage())
                .build();

        return ApiErrorResponse.of(NOT_FOUND.value(), NOT_FOUND, httpRequest.getRequestURI(), errorResult);
    }

    // HTTP 요청 파라미터가 누락 혹은 이상있는경우.  ex) /orders?keyword=2 -> /orders?keywo=2
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiErrorResponse<ErrorResult> handleMissingServletRequestParameterExHandler(MissingServletRequestParameterException e, HttpServletRequest httpRequest) {

        myPrintStackTrace(e.getMessage(), e.getStackTrace());
        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(INVALID_INPUT_VALUE)
                .timestamp(LocalDateTime.now().toString())
                .message(e.getMessage())
                .build();

        return ApiErrorResponse.of(NOT_FOUND.value(), NOT_FOUND, httpRequest.getRequestURI(), errorResult);
    }

    // 서버에러
    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiErrorResponse<String> serverExHandler(Exception e) {

        log.error("[ExceptionHandler] {}", e.getClass());
        myPrintStackTrace(e.getMessage(), e.getStackTrace());

        return ApiErrorResponse.of(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, null, null);
    }

    private static void myPrintStackTrace(String e, StackTraceElement[] elements) {
        log.error("[ExceptionHandler] {} ", e);
        for (StackTraceElement element : elements) {
            log.error("[ExceptionHandler] {} ", element);
        }
    }
}
