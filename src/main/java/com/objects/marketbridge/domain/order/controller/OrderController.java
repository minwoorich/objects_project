package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.KakaoPayReadyResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.order.service.KakaoPayReadyService;
import com.objects.marketbridge.domain.payment.config.KakaoPayConfig;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
import com.objects.marketbridge.model.Address;
import com.objects.marketbridge.model.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.objects.marketbridge.global.error.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final MemberRepository memberRepository;
    private final CreateOrderService createOrderService;
    private final KakaoPayConfig kakaoPayConfig;
    private final KakaoPayReadyService kakaoPayReadyService;

    @GetMapping("/orders/checkout")
    public ApiResponse<CheckoutResponse> getCheckout(
            @AuthMemberId Long memberId) {

        Member member = memberRepository.findByIdWithAddresses(memberId).orElseThrow(EntityNotFoundException::new);
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

        // 1. kakaoPaymentReadyService 호출
        KakaoPayReadyResponse response = kakaoPayReadyService.execute(createKakaoReadyRequest(request, memberId));
        String tid = response.getTid();

        // 2. 주문 생성
        createOrderService.create(getCreateOrderDto(request, memberId, tid));

        return ApiResponse.ok(response);
    }

    private CreateOrderDto getCreateOrderDto(CreateOrderRequest request, Long memberId, String tid) {

        return CreateOrderDto.fromRequest(request, memberId, tid);
    }
    private KakaoPayReadyRequest createKakaoReadyRequest(CreateOrderRequest request, Long memberId) {

        String cid = kakaoPayConfig.getCid();
        String cancelUrl = kakaoPayConfig.getCancelUrl();
        String failUrl = kakaoPayConfig.getFailUrl();
        String approvalUrl = kakaoPayConfig.getApprovalUrl();

        return request.toKakaoReadyRequest(memberId, cid, approvalUrl, failUrl, cancelUrl);
    }
}
