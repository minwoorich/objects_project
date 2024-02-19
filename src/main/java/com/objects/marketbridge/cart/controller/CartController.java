package com.objects.marketbridge.cart.controller;

import com.objects.marketbridge.cart.controller.dto.CreateCartHttp;
import com.objects.marketbridge.cart.controller.dto.UpdateCartHttp;
import com.objects.marketbridge.cart.service.AddToCartService;
import com.objects.marketbridge.cart.service.GetCartListService;
import com.objects.marketbridge.cart.service.dto.GetCartDto;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.interceptor.SliceResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final AddToCartService addToCartService;
    private final GetCartListService getCartListService;

    @PostMapping("/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> addToCart(
            @RequestBody CreateCartHttp.Request request,
            @AuthMemberId Long memberId) {

        addToCartService.add(request.toDto(memberId));

        return ApiResponse.create();
    }

    @GetMapping("/carts")
    public ApiResponse<SliceResponse<GetCartDto>> getCartItems(
            @PageableDefault(value = 5, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthMemberId Long memberId) {

        return ApiResponse.ok(getCartListService.get(pageable, memberId));
    }

    @GetMapping("/carts/count")
    public ApiResponse<Long> countCartItems(
            @AuthMemberId Long memberId) {

        // TODO : 카운트 쿼리 날리는 서비스 작성
        Long result = 1L;
        return ApiResponse.ok(result);
    }

    @PatchMapping("/carts/{cartId}")
    public ApiResponse<String> updateCartItems(
            @PathVariable(name = "cartId") Long cartId,
            @RequestBody UpdateCartHttp.Request request,
            @AuthMemberId Long memberId) {

        // TODO : 장바구니 수량 수정하는 서비스 작성
        return ApiResponse.ok("update success");
    }

    @DeleteMapping("/carts/{cartId}")
    public ApiResponse<String> deleteCartItem(
            @AuthMemberId Long memberId,
            @PathVariable("cartId") Long cartId) {

        // TODO : 장바구니 삭제하는 서비스 작성
        return ApiResponse.ok("delete success");
    }

    @DeleteMapping("/carts")
    public ApiResponse<String> deleteAllCartItems(
            @AuthMemberId Long memberId,
            @PathVariable("cartId") Long cartId) {

        // TODO : 장바구니 삭제하는 서비스 작성
        return ApiResponse.ok("delete success");
    }
}
