package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.request.OrderCancelRequest;
import com.objects.marketbridge.domain.order.controller.response.OrderCancelResponse;
import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnResponse;
import com.objects.marketbridge.domain.order.service.OrderCancelReturnService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderCancelController {

    private final OrderCancelReturnService orderCancelReturnService;

    @PostMapping("/orders/cancel-return-flow/thank-you")
    public ApiResponse<OrderCancelReturnResponse> cancelReturnOrder(@RequestBody @Valid OrderCancelRequest request) {
        return ApiResponse.ok(orderCancelReturnService.cancelReturnOrder(request.toServiceRequest(), LocalDateTime.now()));
    }

    @GetMapping("/orders/cancel-flow")
    public ApiResponse<OrderCancelResponse> requestCancelOrder(
            @RequestParam(name = "orderId") Long orderId,
            @RequestParam(name = "productIds") List<Long> productIds
    ) {
        return ApiResponse.ok(orderCancelReturnService.requestCancel(orderId, productIds));
    }

}
