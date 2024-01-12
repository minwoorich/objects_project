package com.objects.marketbridge.domain.payment.controller;

import com.objects.marketbridge.domain.payment.controller.response.TossPaymentResponse;
import com.objects.marketbridge.global.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TossController {
    @GetMapping("/payments/toss/success")
    public ApiResponse<TossPaymentResponse> tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam Long orderId,
            @RequestParam Long amount) {

        return null;

    }
}
