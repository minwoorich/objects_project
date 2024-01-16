package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Point;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderService createOrderService;
    private final MemberRepository memberRepository;

    @GetMapping("/orders/checkout")
    public ApiResponse<CheckoutResponse> showCheckout(@SessionAttribute Long memberId) {

        Member member = memberRepository.findByIdWithPointAndAddresses(memberId);
        List<Address> addresses = member.getAddresses();
        Point point = member.getPoint();

        CheckoutResponse checkoutResponse = CheckoutResponse.builder()
                .addressList(addresses.stream().map(Address::getAddressValue).collect(Collectors.toList()))
                .pointBalance(point.getBalance()).build();

        return ApiResponse.ok(checkoutResponse);
    }

    @PostMapping("/orders")
    public ApiResponse<CreateOrderResponse> createOrder(@SessionAttribute Long memberId, @Valid @RequestBody CreateOrderRequest createOrderRequest) {

        String orderNo = UUID.randomUUID().toString();

        CreateOrderResponse resp = createOrderService.create(
                createOrderRequest.toProdOrderDto(memberId, orderNo),
                createOrderRequest.toProdOrderDetailDtos());

        return ApiResponse.ok(resp);
    }
}
