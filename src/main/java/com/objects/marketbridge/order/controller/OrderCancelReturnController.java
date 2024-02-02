package com.objects.marketbridge.order.controller;


import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.controller.dto.ConfirmCancelReturnHttp;
import com.objects.marketbridge.order.controller.response.OrderCancelReturnDetailResponse;
import com.objects.marketbridge.order.infra.dtio.CancelReturnResponseDtio;
import com.objects.marketbridge.order.controller.response.OrderReturnResponse;
import com.objects.marketbridge.order.service.OrderCancelReturnService;
import com.objects.marketbridge.order.service.dto.RequestCancelHttp;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.objects.marketbridge.common.domain.MembershipType.WOW;

@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderCancelReturnController {

    private final OrderCancelReturnService orderCancelReturnService;
    private final OrderDtoRepository orderDtoRepository;
    private final DateTimeHolder dateTimeHolder;

    @PostMapping("/cancel-return-flow/thank-you")
    public ApiResponse<ConfirmCancelReturnHttp.Response> confirmCancelReturn(@RequestBody @Valid ConfirmCancelReturnHttp.Request request) {
        return ApiResponse.ok(ConfirmCancelReturnHttp.Response.of(orderCancelReturnService.confirmCancelReturn(request.toServiceRequest(), dateTimeHolder)));
    }

    @GetMapping("/cancel-flow")
    public ApiResponse<RequestCancelHttp.Response> requestCancel(
            @RequestParam(name = "orderNo") String orderNo,
            @RequestParam(name = "productIds") List<Long> productIds
    ) {
        return ApiResponse.ok(RequestCancelHttp.Response.of(orderCancelReturnService.findCancelInfo(orderNo, productIds, WOW.getText())));
    }

    @GetMapping("/return-flow")
    public ApiResponse<OrderReturnResponse> requestReturnOrder(
            @RequestParam(name = "orderNo") String orderNo,
            @RequestParam(name = "productIds") List<Long> productIds
    ) {
        return ApiResponse.ok(OrderReturnResponse.of(orderCancelReturnService.requestReturn(orderNo, productIds, WOW.getText())));
    }

    @GetMapping("/cancel-return/list")
    public ApiResponse<Page<CancelReturnResponseDtio>> getCancelReturnList(
            @RequestParam(name = "memberId") Long memberId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResponse.ok(orderDtoRepository.findOrdersByMemberId(memberId, pageRequest));
    }

    @GetMapping("/cancel-return/{orderNo}")
    public ApiResponse<OrderCancelReturnDetailResponse> getCancelReturnDetail(
            @PathVariable(name = "orderNo") String orderNo,
            @RequestParam(name = "productIds") List<Long> productIds
    ) {
        return ApiResponse.ok(OrderCancelReturnDetailResponse.of(orderCancelReturnService.findCancelReturnDetail(orderNo, productIds, WOW.getText(), dateTimeHolder)));
    }
}
