package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Point;
import com.objects.marketbridge.domain.order.controller.request.TempOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.domain.OrderTemp;
import com.objects.marketbridge.domain.order.exception.exception.CustomLogicException;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.global.common.ApiResponse;
import com.objects.marketbridge.global.utils.GroupingHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.objects.marketbridge.domain.order.exception.exception.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/orders/checkout")
    public ApiResponse<CheckoutResponse> getCheckout(
            @SessionAttribute Long memberId) {

        Member member = memberRepository.findByIdWithPointAndAddresses(memberId);
        CheckoutResponse checkoutResponse = createOrderResponse(member);

        return ApiResponse.ok(checkoutResponse);
    }

    private CheckoutResponse createOrderResponse(Member member) {

        Address address = filterDefaultAddress(member.getAddresses());
        Point point = member.getPoint();

        return CheckoutResponse.from(address.getAddressValue(), point.getBalance());
    }

    private Address filterDefaultAddress(List<Address> addresses) {
        return addresses.stream()
                .filter(Address::isDefault)
                .findFirst()
                .orElseThrow(() -> new CustomLogicException(SHIPPING_ADDRESS_NOT_REGISTERED.getMessage(), SHIPPING_ADDRESS_NOT_REGISTERED));
    }

    @PostMapping("/orders/checkout")
    public ApiResponse<String> saveOrderTemp(
            @SessionAttribute(name="memberId") Long memberId,
            @Valid @RequestBody TempOrderRequest request) {

        List<OrderTemp> orderTemps = createOrderTempList(request);
        orderRepository.saveOrderTempAll(orderTemps);

        return ApiResponse.ok("");
    }

    private List<OrderTemp> createOrderTempList(TempOrderRequest request) {
        return request.getProducts().stream().map(p ->
                new OrderTemp(request.getOrderId(), request.getAmount(), p)
        ).toList();
    }
}
