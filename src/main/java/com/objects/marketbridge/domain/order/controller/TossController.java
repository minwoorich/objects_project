package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.order.service.TossApiService;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.dto.TossConfirmRequest;
import com.objects.marketbridge.domain.payment.service.CreatePaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TossController {

    private final CreateOrderService createOrderService;
    private final TossApiService tossApiService;

    @GetMapping("/payments/toss/success")
    public ApiResponse<TossPaymentsResponse> tossPaymentSuccess(
            @AuthMemberId Long memberId,
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam(name ="amount") Long totalOrderPrice) {


        // 1. 결제 요청
        TossPaymentsResponse tossPaymentsResponse = tossApiService.requestPaymentAccept(memberId, new TossConfirmRequest(paymentKey, orderNo, totalOrderPrice));

        // 2. 주문 생성
//        createOrderService.create(new CreateOrderDto());
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
