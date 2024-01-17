package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Point;
import com.objects.marketbridge.domain.order.controller.request.CheckoutRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.entity.OrderTemp;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
import com.objects.marketbridge.global.security.annotation.UserAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.objects.marketbridge.global.error.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final TossPaymentConfig tossPaymentConfig;

    @GetMapping("/orders/checkout")
    public ApiResponse<CheckoutResponse> getCheckout(
            @AuthMemberId Long memberId) {

        Member member = memberRepository.findByIdWithPointAndAddresses(memberId);
        CheckoutResponse checkoutResponse = createOrderResponse(member);

        return ApiResponse.ok(checkoutResponse);
    }

    private CheckoutResponse createOrderResponse(Member member) {

        Address address = filterDefaultAddress(member.getAddresses());
        Point point = member.getPoint();

        return CheckoutResponse.from(
                address.getAddressValue(),
                point.getBalance(),
                tossPaymentConfig.getSuccessUrl(),
                tossPaymentConfig.getFailUrl());
    }

    private Address filterDefaultAddress(List<Address> addresses) {
        return addresses.stream()
                .filter(Address::isDefault)
                .findFirst()
                .orElseThrow(() -> new CustomLogicException(SHIPPING_ADDRESS_NOT_REGISTERED.getMessage(), SHIPPING_ADDRESS_NOT_REGISTERED));
    }

    @UserAuthorize
    @PostMapping("/orders/checkout")
    public ApiResponse<String> saveOrderTemp(
            @Valid @RequestBody CheckoutRequest request) {

        OrderTemp orderTemp = OrderTemp.from(request);
        orderRepository.save(orderTemp);
        return ApiResponse.ok("");
    }
}
