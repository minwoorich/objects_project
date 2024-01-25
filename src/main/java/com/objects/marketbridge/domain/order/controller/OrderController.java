package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.KakaoPaymentReadyResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.dto.KakaoPaymentReadyRequest;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.order.service.KakaoPaymentReadyService;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.config.KakaoPaymentConfig;
import com.objects.marketbridge.domain.payment.service.PaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
import com.objects.marketbridge.global.security.annotation.UserAuthorize;
import com.objects.marketbridge.model.Address;
import com.objects.marketbridge.model.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.objects.marketbridge.global.error.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberRepository memberRepository;
    private final CreateOrderService createOrderService;
    private final KakaoPaymentConfig kakaoPaymentConfig;
    private final OrderRepository orderRepository;
    private final KakaoPaymentReadyService kakaoPaymentReadyService;

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
    public ApiResponse<KakaoPaymentReadyResponse> saveOrder(
            @AuthMemberId Long memberId,
            HttpSession session,
            @Valid @RequestBody CreateOrderRequest request) {

        // 1. kakaoPaymentReadyService 호출
        KakaoPaymentReadyResponse response = kakaoPaymentReadyService.execute(createKakaoReadyRequest(request, memberId));
        String tid = response.getTid();

        // 2. 주문 생성
        createOrderService.create(getCreateOrderDto(request, memberId, tid));

        // 3. session 에 tid 저장
        session.setAttribute("tid", tid);

        return ApiResponse.ok(response);
    }

    private CreateOrderDto getCreateOrderDto(CreateOrderRequest request, Long memberId, String tid) {

        return CreateOrderDto.fromRequest(request, memberId, tid);
    }
    private KakaoPaymentReadyRequest createKakaoReadyRequest(CreateOrderRequest request, Long memberId) {
        String cid = kakaoPaymentConfig.getCid();
        String cancelUrl = kakaoPaymentConfig.getCancelUrl();
        String failUrl = kakaoPaymentConfig.getFailUrl();
        String approvalUrl = kakaoPaymentConfig.getApprovalUrl();

        return request.toKakaoReadyRequest(memberId, cid, approvalUrl, failUrl, cancelUrl);
    }
}
