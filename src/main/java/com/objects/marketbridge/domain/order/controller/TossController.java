package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.service.TossService;
import com.objects.marketbridge.domain.payment.service.CreatePaymentService;
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
    private final TossService tossService;
    private final MemberRepository memberRepository;

    @GetMapping("/payments/toss/success")
    public ApiResponse<TossPaymentsResponse> tossPaymentSuccess(
            @SessionAttribute Long memberId,
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam(name ="amount") Long totalOrderPrice) {

        TossPaymentsResponse tossPaymentsResponse = tossService.requestPaymentAccept(memberId, paymentKey, orderNo, totalOrderPrice);
//        createPaymentService.create(memberId, paymentKey, orderNo, totalOrderPrice);

        //TODO : orderNo, totalPrice 검증 해야함(그리고 임시로 DB에 저장) -> redis?!

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
