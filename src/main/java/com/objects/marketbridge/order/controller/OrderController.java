package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.security.domain.CustomUserDetails;
import com.objects.marketbridge.order.domain.Address;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.exception.error.CustomLogicException;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.order.service.CreateOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.objects.marketbridge.common.config.KakaoPayConfig.ONE_TIME_CID;
import static com.objects.marketbridge.common.exception.error.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final MemberRepository memberRepository;
    private final CreateOrderService createOrderService;
    private final KakaoPayService kakaoPayService;
    private final KakaoPayConfig kakaoPayConfig;

    @GetMapping("/orders/checkout")
    public ApiResponse<CheckoutResponse> getCheckout(
            @AuthMemberId Long memberId) {

        Member member = memberRepository.findByIdWithAddresses(memberId);
        CheckoutResponse checkoutResponse = createCheckoutResponse(member);

        return ApiResponse.ok(checkoutResponse);
    }

    private CheckoutResponse createCheckoutResponse(Member member) {

        Address address = filterDefaultAddress(member.getAddresses());

        return CheckoutResponse.from(address.getAddressValue());
    }

    private Address filterDefaultAddress(List<Address> addresses) {

        return addresses.stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElseThrow(() -> new CustomLogicException(SHIPPING_ADDRESS_NOT_REGISTERED.getMessage(), SHIPPING_ADDRESS_NOT_REGISTERED));
    }

    @PostMapping("/orders/checkout")
    public ApiResponse<KakaoPayReadyResponse> saveOrder(
            @AuthMemberId Long memberId,
            @Valid @RequestBody CreateOrderRequest request) {

        // 0. orderNo 생성
        String orderNo = UUID.randomUUID().toString();

        // 1. kakaoPaymentReadyService 호출
        KakaoPayReadyRequest kakaoReadyRequest = createKakaoReadyRequest(orderNo, request, memberId);
        KakaoPayReadyResponse response = kakaoPayService.ready(kakaoReadyRequest);
        String tid = response.getTid();

        // 2. 주문 생성
        createOrderService.create(request.toDto(memberId, tid));

        return ApiResponse.ok(response);
    }

    private KakaoPayReadyRequest createKakaoReadyRequest(String orderNo, CreateOrderRequest request, Long memberId) {

        String cid = ONE_TIME_CID;
        String cancelUrl = kakaoPayConfig.getRedirectCancelUrl();
        String failUrl = kakaoPayConfig.getRedirectFailUrl();
        String approvalUrl = kakaoPayConfig.createApprovalUrl("/payment");

        return request.toKakaoReadyRequest(orderNo, memberId, cid, approvalUrl, failUrl, cancelUrl);
    }

    @PostMapping("/test")
    public ApiResponse<String> test(
            @AuthenticationPrincipal CustomUserDetails userDetail) {

        return ApiResponse.ok(userDetail.getEmail());
    }
}
