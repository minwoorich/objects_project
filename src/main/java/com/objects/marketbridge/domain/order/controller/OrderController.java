package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.dto.OrderCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    @PostMapping("/orders/direct/checkout")
    public ResponseEntity<OrderResponse> directOrder(@RequestBody OrderCreate order) {
        return null;
    }





    static class OrderResponse{

    }
}
