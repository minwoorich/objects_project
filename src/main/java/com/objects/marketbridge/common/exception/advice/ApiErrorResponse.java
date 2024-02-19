package com.objects.marketbridge.common.exception.advice;

import com.objects.marketbridge.common.interceptor.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiErrorResponse<T> {
    private int code;
    private HttpStatus status;
    private String path;
    private T error;

    @Builder
    private ApiErrorResponse(int code, HttpStatus status, String path, T error) {
        this.code = code;
        this.status = status;
        this.path = path;
        this.error = error;
    }

    public static <T> ApiErrorResponse<T> of(int code, HttpStatus status, String path, T error) {
        return ApiErrorResponse.<T>builder()
                .code(code)
                .status(status)
                .path(path)
                .error(error)
                .build();

    }
}
