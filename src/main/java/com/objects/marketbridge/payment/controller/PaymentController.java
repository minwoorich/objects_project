package com.objects.marketbridge.payment.controller;

import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.dto.KakaoPayOrderResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.payment.controller.dto.CompleteOrderHttp;
import com.objects.marketbridge.payment.service.CreatePaymentService;
import com.objects.marketbridge.payment.service.QuitPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final CreatePaymentService createPaymentService;
    private final QuitPaymentService quitPaymentService;

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
    public ApiResponse<?> kakaoPaymentApproveFail(@PathVariable(name = "orderNo") String orderNo){
        // TODO : 결제 승인 실패로직 추가해야함

        return ApiResponse.ok(null);
    }

    @GetMapping("/kakao-pay/cancel/{orderNo}") // 결제 승인 취소
    public ApiResponse<KakaoPayOrderResponse> kakaoPaymentApproveCancel(@PathVariable(name = "orderNo") String orderNo){

        KakaoPayOrderResponse response = quitPaymentService.response(orderNo);
        quitPaymentService.cancel(orderNo);

        return ApiResponse.ok(response);
    }


}
