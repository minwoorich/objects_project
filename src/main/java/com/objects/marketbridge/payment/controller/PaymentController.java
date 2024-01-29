package com.objects.marketbridge.payment.controller;

import com.objects.marketbridge.common.dto.KakaoPayApproveRequest;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.dto.KakaoPayOrderResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.service.QuitPaymentService;
import com.objects.marketbridge.payment.service.CreatePaymentService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.objects.marketbridge.common.config.KakaoPayConfig.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentService createPaymentService;
    private final KakaoPayService kakaoPayService;
    private final OrderQueryRepository orderQueryRepository;
    private final QuitPaymentService quitPaymentService;

    @PostMapping("/payment/kakao-pay/approval/{orderNo}")
    public ApiResponse<KakaoPayApproveResponse> kakaoPaymentApproved(
            @RequestParam(name = "pg_token") String pgToken,
            @PathVariable String orderNo) {

        Order order = orderQueryRepository.findByOrderNo(orderNo);
        KakaoPayApproveResponse response = kakaoPayService.approve(createKakaoRequest(order, pgToken));

        // 2. Payment 생성 및 OrderDetails 업데이트
        createPaymentService.create(response);


        return ApiResponse.ok(response);
    }

    @PostMapping("/payment/kakao-pay/fail/{orderNo}")
    public ApiResponse<?> kakaoPaymentFail(@PathVariable(name = "orderNo") String orderNo){

        Order order = orderQueryRepository.findByOrderNo(orderNo);

        KakaoPayOrderResponse response = kakaoPayService.getOrders(order.getTid(), ONE_TIME_CID);
        if ("FAIL_PAYMENT".equals(response.getStatus())) {
            // TODO : quitPaymentService 구현
            quitPaymentService.cancel(response);
        }
        return null;
    }


    private  KakaoPayApproveRequest createKakaoRequest(Order order, String pgToken) {
        return KakaoPayApproveRequest.builder()
                .pgToken(pgToken)
                .partnerOrderId(order.getOrderNo())
                .tid(order.getTid())
                .totalAmount(order.getTotalPrice().toString())
                .cid(ONE_TIME_CID)
                .build();
    }
}
