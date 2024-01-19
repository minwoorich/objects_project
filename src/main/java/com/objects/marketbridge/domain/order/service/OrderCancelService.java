package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.order.controller.response.OrderCancelResponse;
import com.objects.marketbridge.domain.order.dto.OrderCancelServiceDto;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.dto.RefundDto;
import com.objects.marketbridge.domain.payment.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.objects.marketbridge.domain.order.entity.StatusCodeType.*;

@Service
@RequiredArgsConstructor
public class OrderCancelService {

    private final OrderRepository orderRepository;

    private final RefundService refundService;
    
    // TODO 트랜잭션 위치 고려해야함
    @Transactional
    public OrderCancelResponse orderCancel(OrderCancelServiceDto orderCancelServiceDto, LocalDateTime cancelDateTime) {
        InnerService innerService = new InnerService();
        Long orderId = orderCancelServiceDto.getOrderId();
        String reason = orderCancelServiceDto.getReason();

        ProdOrder order = innerService.cancel(orderId, reason, cancelDateTime);

        // TODO 결제 취소(환불)
//        RefundDto refund = refundService.refund("계좌번호", 10000L);

        return OrderCancelResponse.of(order, RefundDto.builder()
                .totalRefundAmount(10000L)
                .refundMethod("card")
                .refundProcessedAt(LocalDateTime.of(2024, 1, 17, 10, 20))
                .build());
    }


    // TODO 객체로 따로 빼야함(임시로 사용)
    class InnerService {
        public ProdOrder cancel(Long orderId, String reason, LocalDateTime cancelDateTime) {
            ProdOrder prodOrder = orderRepository.findProdOrderWithDetailsAndProduct(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 없습니다."));

            prodOrder.cancel(reason, ORDER_CANCEL.getCode(), cancelDateTime);

            prodOrder.returnCoupon();

            return prodOrder;
        }

    }

}
