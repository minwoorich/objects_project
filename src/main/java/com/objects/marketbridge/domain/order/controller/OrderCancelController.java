package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.controller.request.OrderCancelRequest;
import com.objects.marketbridge.domain.order.controller.response.OrderCancelResponse;
import com.objects.marketbridge.domain.order.service.OrderCancelService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderCancelController {

    private OrderCancelService orderCancelService;

    @GetMapping("/orders/cancel-flow")
    public ApiResponse<OrderCancelResponse> cancelOrder(@Valid @RequestBody OrderCancelRequest request) {
        return ApiResponse.of(HttpStatus.OK, "이상없음", new OrderCancelResponse());


    }
}
