package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.order.controller.request.CheckoutRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.order.service.TossApiService;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.domain.payment.dto.TossConfirmRequest;
import com.objects.marketbridge.domain.payment.service.CreatePaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.security.mock.AuthMemberId;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.objects.marketbridge.global.error.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberRepository memberRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final CreateOrderService createOrderService;
    private final CreatePaymentService createPaymentService;
    private final TossApiService tossApiService;

    @GetMapping("/orders/checkout")
    public ApiResponse<CheckoutResponse> getCheckout(
            @AuthMemberId Long memberId) {

//        Long id = memberId;
        Member member = memberRepository.findByIdWithAddresses(memberId).orElseThrow(EntityNotFoundException::new);
        CheckoutResponse checkoutResponse = createOrderResponse(member);

        return ApiResponse.ok(checkoutResponse);
    }

    private CheckoutResponse createOrderResponse(Member member) {

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
    public ApiResponse<CheckoutRequest> saveOrderTemp(
            @AuthMemberId Long memberId,
            @Valid @RequestBody CheckoutRequest request) {

        request.setSuccessUrl(tossPaymentConfig.getSuccessUrl());
        request.setFailUrl(tossPaymentConfig.getFailUrl());

        CreateOrderDto createOrderDto = request.toDto(memberId);
        createOrderService.create(createOrderDto);

        return ApiResponse.ok(request);
    }

    @GetMapping("/payments/toss/success")
    public ApiResponse<TossPaymentsResponse> tossPaymentSuccess(
            @AuthMemberId Long memberId,
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam(name ="amount") Long totalOrderPrice) {


        // 1. 결제 요청
        TossPaymentsResponse tossPaymentsResponse =
                tossApiService.requestPaymentAccept(new TossConfirmRequest(paymentKey, orderNo, totalOrderPrice));

        // 2. Payment 생성
        createPaymentService.create(tossPaymentsResponse);

        // 3.
        // TODO : 1) 판매자 금액 추가(실제입금은 배치로 들어가겠지만, 우선 어딘가에 판매자의 돈이 올라갔음을 저장해놔야함)
        //  TODO : 2) 재고 감소
        // TODO : 3) 쿠폰 사용시 쿠폰 감소
        // TODO : 4) 배송 엔티티 생성 되어야함
        // TODO  :5) 주문 상태 업데이트
        // TODO : 6) 결제 실패시 어떻게 처리?

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
