package com.objects.marketbridge.payment.controller;

import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.dto.KakaoPayApproveRequest;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final KakaoPayService kakaoPayService;
    private final OrderCommendRepository orderCommendRepository;
    private final OrderQueryRepository orderQueryRepository;

//    @PostMapping("/kakao-pay/approval/{orderNo}")
//    public ApiResponse<KakaoPayApproveResponse> kakaoPaymentApproved(
//            @RequestParam(name = "pg_token") String pgToken,
//            @PathVariable String orderNo) {
//
//        Order order = orderRepository.findByOrderNo(orderNo);
//        KakaoPayApproveResponse response = kakaoPayService.approve(createKakaoRequest(order));
//
//        // 2. Payment 생성 및 OrderDetails 업데이트
//        paymentService.create(response);
//
//        // 3.
//        // TODO : 1) 판매자 금액 추가(실제입금은 배치로 들어가겠지만, 우선 어딘가에 판매자의 돈이 올라갔음을 저장해놔야함)
//        // TODO : 6) 결제 실패시 어떻게 처리?
//
//        return ApiResponse.ok(response);
//    }

    @PostMapping("/kakao-pay/approval/{orderNo}")
    public ApiResponse<KakaoPayApproveResponse> kakaoPaymentApproved(
            @RequestParam(name = "pg_token") String pgToken,
            @PathVariable String orderNo) {

        Order order = orderQueryRepository.findByOrderNo(orderNo);
        KakaoPayApproveResponse response = kakaoPayService.approve(createKakaoRequest(order));

        // 2. Payment 생성 및 OrderDetails 업데이트
        paymentService.create(response);

        // 3.
        // TODO : 1) 판매자 금액 추가(실제입금은 배치로 들어가겠지만, 우선 어딘가에 판매자의 돈이 올라갔음을 저장해놔야함)
        // TODO : 6) 결제 실패시 어떻게 처리?

        return ApiResponse.ok(response);
    }

    private  KakaoPayApproveRequest createKakaoRequest(Order order) {
        return KakaoPayApproveRequest.builder()
                .partnerOrderId(order.getOrderNo())
                .tid(order.getTid())
                .totalAmount(order.getTotalPrice().toString())
                .cid(KakaoPayConfig.ONE_TIME_CID)
                .build();
    }


    @PostMapping("/kakao-pay/fail")
    public ApiResponse<?> kakaoPaymentFail(
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam Long amount) {

        return null;
    }
}
