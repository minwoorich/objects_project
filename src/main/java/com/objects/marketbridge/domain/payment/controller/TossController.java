package com.objects.marketbridge.domain.payment.controller;

import com.objects.marketbridge.domain.payment.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.payment.service.CreatePaymentService;
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

        //TODO : tossPaymentsResponse 가 실패했을 수도 있으니 그것에 대한 코드도 만들어놔야함
        // 결제인증실패 (/payments/toss/fail) 와 결제실패 는 엄연히 서로 다른 로직임
        // '결제인증실패' 는 카드사에서 인증을 거부 한거고,
        // '결제실패' 는 토스에서 거부당한것

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
