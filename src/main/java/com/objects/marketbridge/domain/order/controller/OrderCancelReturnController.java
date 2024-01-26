//package com.objects.marketbridge.domain.order.controller;
//
//import com.objects.marketbridge.domain.order.controller.request.OrderCancelRequest;
//import com.objects.marketbridge.domain.order.controller.response.OrderCancelResponse;
//import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnDetailResponse;
//import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnListResponse;
//import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnResponse;
//import com.objects.marketbridge.domain.order.dto.OrderReturnResponse;
//import com.objects.marketbridge.domain.order.service.OrderCancelReturnService;
//import com.objects.marketbridge.global.common.ApiResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class OrderCancelReturnController {
//
//    private final OrderCancelReturnService orderCancelReturnService;
//
//    @PostMapping("/orders/cancel-return-flow/thank-you")
//    public ApiResponse<OrderCancelReturnResponse> cancelReturnOrder(@RequestBody @Valid OrderCancelRequest request) {
//        return ApiResponse.ok(orderCancelReturnService.cancelReturnOrder(request.toServiceRequest(), LocalDateTime.now()));
//    }
//
//    @GetMapping("/orders/cancel-flow")
//    public ApiResponse<OrderCancelResponse> requestCancelOrder(
//            @RequestParam(name = "orderId") Long orderId,
//            @RequestParam(name = "productIds") List<Long> productIds
//    ) {
//        return ApiResponse.ok(orderCancelReturnService.requestCancel(orderId, productIds));
//    }
//
//    @GetMapping("/orders/return-flow")
//    public ApiResponse<OrderReturnResponse> requestReturnOrder(
//            @RequestParam(name = "orderId") Long orderId,
//            @RequestParam(name = "productIds") List<Long> productIds
//    ) {
//        return ApiResponse.ok(orderCancelReturnService.requestReturn(orderId, productIds));
//    }
//
//    @GetMapping("/orders/cancel-return/list")
//    public ApiResponse<Page<OrderCancelReturnListResponse>> getCancelReturnList(
//            @RequestParam(name = "memberId") Long memberId,
//            @RequestParam(name = "page") Integer page,
//            @RequestParam(name = "size") Integer size) {
//
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return ApiResponse.ok(orderCancelReturnService.findCancelReturnList(memberId, pageRequest));
//    }
//
//    @GetMapping("/orders/cancel-return/{orderNo}")
//    public ApiResponse<OrderCancelReturnDetailResponse> getCancelReturnDetail(
//            @PathVariable(name = "orderNo") String orderNo,
//            @RequestParam(name = "paymentId") Long paymentId,
//            @RequestParam(name = "receiptType") String receiptType,
//            @RequestParam(name = "productIds") List<Long> productIds
//    ) {
//        return ApiResponse.ok(orderCancelReturnService.findCancelReturnDetail(orderNo, paymentId, productIds));
//    }
//}
