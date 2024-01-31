package com.objects.marketbridge.order.controller;


import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.order.controller.request.OrderCancelRequest;
import com.objects.marketbridge.order.controller.response.OrderCancelResponse;
import com.objects.marketbridge.order.controller.response.OrderCancelReturnDetailResponse;
import com.objects.marketbridge.order.infra.dtio.CancelReturnResponseDao;
import com.objects.marketbridge.order.controller.response.OrderCancelReturnResponse;
import com.objects.marketbridge.order.service.OrderCancelReturnService;
import com.objects.marketbridge.order.controller.response.OrderReturnResponse;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderCancelReturnController {

    private final OrderCancelReturnService orderCancelReturnService;
    private final OrderDtoRepository orderDtoRepository;

    @PostMapping("/orders/cancel-return-flow/thank-you")
    public ApiResponse<OrderCancelReturnResponse> cancelReturnOrder(@RequestBody @Valid OrderCancelRequest request) {
        return ApiResponse.ok(OrderCancelReturnResponse.of(orderCancelReturnService.confirmCancelReturn(request.toServiceRequest())));
    }

    @GetMapping("/orders/cancel-flow")
    public ApiResponse<OrderCancelResponse> requestCancelOrder(
            @RequestParam(name = "orderId") Long orderId,
            @RequestParam(name = "productIds") List<Long> productIds
    ) {
        return ApiResponse.ok(OrderCancelResponse.of(orderCancelReturnService.requestCancel(orderId, productIds)));
    }

    @GetMapping("/orders/return-flow")
    public ApiResponse<OrderReturnResponse> requestReturnOrder(
            @RequestParam(name = "orderId") Long orderId,
            @RequestParam(name = "productIds") List<Long> productIds
    ) {
        return ApiResponse.ok(OrderReturnResponse.of(orderCancelReturnService.requestReturn(orderId, productIds)));
    }

    @GetMapping("/orders/cancel-return/list")
    public ApiResponse<Page<CancelReturnResponseDao>> getCancelReturnList(
            @RequestParam(name = "memberId") Long memberId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResponse.ok(orderDtoRepository.findOrdersByMemberId(memberId, pageRequest));
    }

    @GetMapping("/orders/cancel-return/{orderNo}")
    public ApiResponse<OrderCancelReturnDetailResponse> getCancelReturnDetail(
            @PathVariable(name = "orderNo") String orderNo,
            @RequestParam(name = "paymentId") Long paymentId,
            @RequestParam(name = "receiptType") String receiptType,
            @RequestParam(name = "productIds") List<Long> productIds
    ) {
        return ApiResponse.ok(OrderCancelReturnDetailResponse.of(orderCancelReturnService.findCancelReturnDetail(orderNo, paymentId, productIds)));
    }
}
