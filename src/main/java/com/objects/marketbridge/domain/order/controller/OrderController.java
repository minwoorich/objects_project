package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.global.security.annotation.UserAuthorize;
import com.objects.marketbridge.model.Address;
import com.objects.marketbridge.model.Member;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.order.service.TossApiService;
import com.objects.marketbridge.domain.payment.dto.TossConfirmRequest;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
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
    private final CreateOrderService createOrderService;
    private final TossApiService tossApiService;
    private final OrderRepository orderRepository;

    @GetMapping("/orders/checkout")
    public ApiResponse<CheckoutResponse> getCheckout(
            @AuthMemberId Long memberId) {

//        Long id = memberId;
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
    public ApiResponse<CreateOrderResponse> saveOrder(
            @AuthMemberId Long memberId,
            @Valid @RequestBody CreateOrderRequest request) {

        CreateOrderDto createOrderDto = CreateOrderDto.fromRequest(request, memberId);

        return ApiResponse.ok(createOrderService.create(createOrderDto));
    }

    @UserAuthorize
    @DeleteMapping("/orders/checkout")
    public ApiResponse<String> saveOrderRollback(@RequestParam String orderNo) {

        orderRepository.deleteByOrderNo(orderNo);

        return ApiResponse.ok(StatusCodeType.PAYMENT_CANCEL.toString());
    }

    @GetMapping("/orders/toss-payments/success")
    public ApiResponse<TossPaymentsResponse> tossPaymentSuccess(
            @AuthMemberId Long memberId,
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam(name ="amount") Long totalOrderPrice) {


        // 1. 결제 요청
        TossPaymentsResponse tossPaymentsResponse =
                tossApiService.requestPaymentAccept(new TossConfirmRequest(paymentKey, orderNo, totalOrderPrice));

        // 2. Order, OrderDetail, Payment 생성
        // TODO : 레디스에서 임시 데이터 불러온다음, createOrderDto 만들어줘야함
//        createOrderService.create(createOrderDto, tossPaymentsResponse);

        // 3.
        // TODO : 1) 판매자 금액 추가(실제입금은 배치로 들어가겠지만, 우선 어딘가에 판매자의 돈이 올라갔음을 저장해놔야함)
        //  TODO : 2) 재고 감소
        // TODO : 3) 쿠폰 사용시 쿠폰 감소
        // TODO : 4) 배송 엔티티 생성 되어야함
        // TODO  :5) 주문 상태 업데이트
        // TODO : 6) 결제 실패시 어떻게 처리?

        return ApiResponse.ok(tossPaymentsResponse);
    }

    @GetMapping("/orders/toss-payments/fail")
    public ApiResponse<TossPaymentsResponse> tossPaymentFail(
            @RequestParam String paymentKey,
            @RequestParam(name = "orderId") String orderNo,
            @RequestParam Long amount) {

        return null;
    }
}
