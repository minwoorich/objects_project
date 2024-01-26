package com.objects.marketbridge.domain.payment.controller;

import com.objects.marketbridge.domain.payment.config.KakaoPayConfig;
import com.objects.marketbridge.domain.payment.controller.response.KakaoPayApproveResponse;
import com.objects.marketbridge.domain.payment.service.KakaoPaymentApproveService;
import com.objects.marketbridge.domain.payment.service.PaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.error.ErrorCode;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentConroller {

    private final PaymentService paymentService;
    private final KakaoPaymentApproveService kakaoPaymentApproveService;
    private final KakaoPayConfig kakaoPayConfig;

    @PostMapping("/payments/kakao/approval")
    public ApiResponse<KakaoPayApproveResponse> kakaoPaymentApproved(
            @AuthMemberId Long memberId,
            @RequestParam(name = "pg_token") String pgToken,
            HttpSession session) {

        // 1. 결제 승인 요청
        if (session == null) {
            throw new CustomLogicException(ErrorCode.SESSION_EXPIRED.getMessage());
        }

        KakaoPayApproveResponse response = kakaoPaymentApproveService.execute(
                pgToken,
                memberId,
                (String) session.getAttribute("tid"),
                kakaoPayConfig.getCid());
        session.invalidate();

        // 2. Payment 생성 및 OrderDetails 업데이트
        paymentService.create(response);

        // 3.
        // TODO : 1) 판매자 금액 추가(실제입금은 배치로 들어가겠지만, 우선 어딘가에 판매자의 돈이 올라갔음을 저장해놔야함)
        // TODO : 6) 결제 실패시 어떻게 처리?

        return ApiResponse.ok(response);
    }



    @PostMapping("/payments/kakao/fail")
    public ApiResponse<?> kakaoPaymentFail(
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam Long amount) {

        return null;
    }
}
