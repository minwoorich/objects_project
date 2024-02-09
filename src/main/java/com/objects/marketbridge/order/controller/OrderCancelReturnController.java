package com.objects.marketbridge.order.controller;


import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.*;
import com.objects.marketbridge.order.service.OrderCancelReturnService;
import com.objects.marketbridge.order.service.port.OrderDetailDtoRepository;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderCancelReturnController {

    private final OrderCancelReturnService orderCancelReturnService;
    private final OrderDetailDtoRepository orderDetailDtoRepository;
    private final MemberRepository memberRepository;
    private final DateTimeHolder dateTimeHolder;

    @PostMapping("/cancel-return-flow/thank-you")
    public ApiResponse<ConfirmCancelHttp.Response> confirmCancelReturn(
            @RequestBody @Valid ConfirmCancelHttp.Request request
    ) {
        return ApiResponse.ok(ConfirmCancelHttp.Response.of(orderCancelReturnService.confirmCancelReturn(request.toDto(), dateTimeHolder)));
    }

    @GetMapping("/cancel-flow")
    public ApiResponse<RequestCancelHttp.Response> requestCancel(
            @RequestParam(name = "orderDetailId") Long orderDetailId,
            @RequestParam(name = "numberOfCancellation") Long numberOfCancellation,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(RequestCancelHttp.Response.of(orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership)));
    }

    @GetMapping("/return-flow")
    public ApiResponse<RequestReturnHttp.Response> requestReturn (
            @RequestParam(name = "orderDetailId") Long orderDetailId,
            @RequestParam(name = "numberOfReturns") Long numberOfReturns,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(RequestReturnHttp.Response.of(orderCancelReturnService.findReturnInfo(orderDetailId, numberOfReturns, membership)));
    }

    @GetMapping("/cancel-return/list")
    public ApiResponse<Page<GetCancelReturnListHttp.Response>> getCancelReturnList (
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @AuthMemberId Long memberId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResponse.ok(GetCancelReturnListHttp.Response.of(orderDetailDtoRepository.findCancelReturnListDtio(memberId, pageRequest)));
    }

    @GetMapping("/cancel/detail")
    public ApiResponse<GetCancelDetailHttp.Response> getCancelDetail(
            @RequestParam(name = "orderDetailId") Long orderDetailId,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(GetCancelDetailHttp.Response.of(orderCancelReturnService.findCancelDetail(orderDetailId, membership)));
    }

    @GetMapping("/return/detail")
    public ApiResponse<GetReturnDetailHttp.Response> getReturnDetail(
            @RequestParam(name = "orderDetailId") Long orderDetailId,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(GetReturnDetailHttp.Response.of(orderCancelReturnService.findReturnDetail(orderDetailId, membership)));
    }

    // 반품 철회 확정
    @PostMapping("/cancel-return/list")
    public ApiResponse<ReturnRecantationHttp.Response> returnRecantation(
            @RequestParam(name = "orderDetailId") Long orderDetailId
    ) {
        return null;
    }


}
