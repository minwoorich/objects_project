package com.objects.marketbridge.cart.controller;

import com.objects.marketbridge.cart.controller.dto.CreateCartHttp;
import com.objects.marketbridge.cart.service.CreateCartService;
import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CreateCartService createCartService;

    @PostMapping("/carts")
    public ApiResponse<String> createCart(
            @RequestBody CreateCartHttp.Request request,
            @AuthMemberId Long memberId) {
        Boolean result = createCartService.create(request.toDto(memberId));
        if(!result){
            throw CustomLogicException.createBadRequestError(DUPLICATE_OPERATION, "이미 장바구니에 담긴 상품입니다", LocalDateTime.now());
        }
        return ApiResponse.create();
    }
}
