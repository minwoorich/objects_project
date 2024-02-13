package com.objects.marketbridge.cart.controller;

import com.objects.marketbridge.cart.controller.dto.CreateCartHttp;
import com.objects.marketbridge.cart.service.AddToCartService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final AddToCartService addToCartService;

    @PostMapping("/carts")
    public ApiResponse<String> addToCart(
            @RequestBody CreateCartHttp.Request request,
            @AuthMemberId Long memberId) {

        addToCartService.add(request.toDto(memberId));

        return ApiResponse.create();
    }
}
