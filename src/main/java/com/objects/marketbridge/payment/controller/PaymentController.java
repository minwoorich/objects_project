package com.objects.marketbridge.payment.controller;

import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.exception.advice.ApiErrorResponse;
import com.objects.marketbridge.common.exception.advice.ErrorResult;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.payment.controller.dto.CancelledPaymentHttp;
import com.objects.marketbridge.payment.controller.dto.CompleteOrderHttp;
import com.objects.marketbridge.payment.service.CreatePaymentService;
import com.objects.marketbridge.payment.service.QuitPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final CreatePaymentService createPaymentService;
    private final QuitPaymentService quitPaymentService;

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiErrorResponse<ErrorResult> kakaoApiExHandler(Exception e, HttpServletRequest httpRequest) {
        log.error("[exceptionHandler] {} ", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("[exceptionHandler] {} ", element);
        }

        String requestURI = httpRequest.getRequestURI();
        String[] uri = requestURI.split("/");
        String orderNo = uri[uri.length - 1];
        quitPaymentService.cancel(orderNo);

        return ApiErrorResponse.of(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, null, null);
    }

    @GetMapping("/payment/kakao-pay/approval/{orderNo}") // 결제 승인 성공
    public ApiResponse<CompleteOrderHttp.Response> createPayment(
            @RequestParam(name = "pg_token") String pgToken,
            @PathVariable(name = "orderNo") String orderNo) {

        // 1. kakaoPayService 로 결제 승인 요청 보내기
        KakaoPayApproveResponse kakaoResponse = createPaymentService.approve(orderNo, pgToken);

        // 2. Payment 생성 및 OrderDetails 업데이트
        CompleteOrderHttp.Response response = createPaymentService.create(kakaoResponse);

        return ApiResponse.ok(response);
    }

    @GetMapping("/kakao-pay/fail/{orderNo}") // 결제 승인 실패
    public ApiResponse<?> kakaoPaymentApproveFail(
            @PathVariable(name = "orderNo") String orderNo){
        // TODO : 결제 승인 실패로직 추가해야함

        return ApiResponse.ok(null);
    }

    @GetMapping("/kakao-pay/cancel/{orderNo}") // 결제 승인 취소
    public ApiResponse<CancelledPaymentHttp.Response> kakaoPaymentApproveCancel(
            @PathVariable(name = "orderNo") String orderNo){

        CancelledPaymentHttp.Response response = quitPaymentService.response(orderNo);
        quitPaymentService.cancel(orderNo);

        return ApiResponse.ok(response);
    }


}
