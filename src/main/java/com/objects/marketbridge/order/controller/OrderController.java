package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.order.controller.dto.CreateCheckoutHttp;
import com.objects.marketbridge.order.controller.dto.CreateOrderHttp;
import com.objects.marketbridge.order.controller.dto.GetOrderHttp;
import com.objects.marketbridge.order.service.CreateCheckoutService;
import com.objects.marketbridge.order.service.CreateOrderService;
import com.objects.marketbridge.order.service.GetOrderService;
import com.objects.marketbridge.order.service.dto.OrderDto;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.objects.marketbridge.common.config.KakaoPayConfig.ONE_TIME_CID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CreateOrderService createOrderService;
    private final CreateCheckoutService createCheckoutService;
    private final GetOrderService getOrderService;
    private final KakaoPayService kakaoPayService;
    private final KakaoPayConfig kakaoPayConfig;
    private final OrderDtoRepository orderDtoRepository;

    @GetMapping("/orders/checkout")
    public ApiResponse<CreateCheckoutHttp.Response> createCheckout(
            @AuthMemberId Long memberId) {

        return ApiResponse.ok(createCheckoutService.create(memberId));
    }

    @PostMapping("/orders/checkout")
    public ApiResponse<CreateOrderHttp.Response> createOrder(
            @AuthMemberId Long memberId,
            @Valid @RequestBody CreateOrderHttp.Request createOrderRequest) {

        // 0. orderNo 생성
        String orderNo = UUID.randomUUID().toString();

        // 1. kakaoPaymentReadyService 호출
        KakaoPayReadyRequest kakaoReadyRequest = createKakaoReadyRequest(orderNo, createOrderRequest, memberId);
        KakaoPayReadyResponse kakaoReadyResponse = kakaoPayService.ready(kakaoReadyRequest);
        String tid = kakaoReadyResponse.getTid();

        // 2. 주문 생성
        createOrderService.create(createOrderRequest.toDto(orderNo, tid, memberId));

        return ApiResponse.ok(CreateOrderHttp.Response.of(kakaoReadyResponse));
    }

    private KakaoPayReadyRequest createKakaoReadyRequest(String orderNo, CreateOrderHttp.Request request, Long memberId) {

        String cid = ONE_TIME_CID;
        String cancelUrl = kakaoPayConfig.getRedirectCancelUrl();
        String failUrl = kakaoPayConfig.getRedirectFailUrl();
        String approvalUrl = kakaoPayConfig.createApprovalUrl("/payment");

        return request.toKakaoReadyRequest(orderNo, memberId, cid, approvalUrl, failUrl, cancelUrl);
    }

//    // TODO : 전체 주문 목록 조회 컨트롤러 완성해야함
//    @GetMapping("/orders/list")
//    public ApiResponse<GetOrderHttp.Response> getOrders(
//            @AuthMemberId Long memberId,
//            GetOrderHttp.Condition condition,
//            Pageable pageable
//    ) {
//        condition.setMemberId(memberId);
//        GetOrderHttp.Response response = getOrderService.find(pageable, condition);
//        return null;
//    }
    @GetMapping("/orders/list")
    public ApiResponse<Page<OrderDto>> getOrders(
            @AuthMemberId Long memberId,
            GetOrderHttp.Condition condition,
            Pageable pageable
    ) {
        condition.setMemberId(memberId);
        Page<OrderDto> orderDtos = orderDtoRepository.findByMemberIdWithMemberAddress(condition, pageable);
        return ApiResponse.ok(orderDtos);
    }
}
