package com.objects.marketbridge.domain.payment.controller;

import com.objects.marketbridge.domain.payment.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.payment.service.CreatePaymentService;
import com.objects.marketbridge.domain.payment.service.PaymentService;
import com.objects.marketbridge.domain.payment.service.TossPaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
public class TossController {

    private final CreatePaymentService createPaymentService;
    private final TossPaymentService tossPaymentService;

    @GetMapping("/payments/toss/success")
    public ApiResponse<TossPaymentsResponse> tossPaymentSuccess(
            @SessionAttribute Long memberId,
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam(name ="amount") Long totalPrice) {

        createPaymentService.create(memberId, paymentKey, orderNo, totalPrice);
        TossPaymentsResponse tossPaymentsResponse = tossPaymentService.requestPaymentAccept(memberId, paymentKey, orderNo, totalPrice);

        return ApiResponse.ok(tossPaymentsResponse);

    }

    @GetMapping("/payments/toss/fail")
    public ApiResponse<TossPaymentsResponse> tossPaymentFail(
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam Long amount) {


        return null;

    }
}
