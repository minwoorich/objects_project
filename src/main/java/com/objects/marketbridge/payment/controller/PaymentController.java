package com.objects.marketbridge.payment.controller;

import com.objects.marketbridge.common.dto.KakaoPayApproveRequest;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.dto.KakaoPayOrderResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.controller.dto.CompleteOrderHttp;
import com.objects.marketbridge.payment.service.CreatePaymentService;
import com.objects.marketbridge.payment.service.QuitPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.objects.marketbridge.common.config.KakaoPayConfig.ONE_TIME_CID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final CreatePaymentService createPaymentService;
    private final KakaoPayService kakaoPayService;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/payment/kakao-pay/approval/{orderNo}")
    public ApiResponse<CompleteOrderHttp.Response> createPayment(
            @RequestParam(name = "pg_token") String pgToken,
            @PathVariable(name = "orderNo") String orderNo) {

        // 1. kakaoPayService 로 결제 승인 요청 보내기
        Order order = orderQueryRepository.findByOrderNoWithMember(orderNo);
        KakaoPayApproveResponse kakaoResponse = kakaoPayService.approve(createKakaoRequest(order, pgToken));

        // 2. Payment 생성 및 OrderDetails 업데이트
        CompleteOrderHttp.Response response = createPaymentService.create(kakaoResponse);

        return ApiResponse.ok(response);
    }

//    @GetMapping("/payment/kakao-pay/fail/{orderNo}")
//    public ApiResponse<?> kakaoPaymentFail(@PathVariable(name = "orderNo") String orderNo){
//
//        Order order = orderQueryRepository.findByOrderNo(orderNo);
//
//        KakaoPayOrderResponse response = kakaoPayService.getOrders(order.getTid(), ONE_TIME_CID);
//        if ("FAIL_PAYMENT".equals(response.getStatus())) {
//            // TODO : quitPaymentService 구현
//            quitPaymentService.cancel(response);
//        }
//        return null;
//    }
    @GetMapping("/payment/kakao-pay/fail/{orderNo}")
    public ApiResponse<KakaoPayOrderResponse> kakaoPaymentFail(@PathVariable(name = "orderNo") String orderNo){

        Order order = orderQueryRepository.findByOrderNo(orderNo);
        KakaoPayOrderResponse response = kakaoPayService.getOrders(order.getTid(), ONE_TIME_CID);
        // TODO : 조회 후 status가 FAIL_PAYMENT 일 경우 후처리 로직 만들어야함

        return ApiResponse.ok(response);
    }

    private  KakaoPayApproveRequest createKakaoRequest(Order order, String pgToken) {
        return KakaoPayApproveRequest.builder()
                .pgToken(pgToken)
                .partnerUserId(order.getMember().getId().toString())
                .partnerOrderId(order.getOrderNo())
                .tid(order.getTid())
                .totalAmount(order.getTotalPrice())
                .cid(ONE_TIME_CID)
                .build();
    }
}
