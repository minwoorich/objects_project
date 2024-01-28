package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderCancelReturnResponse {

    private Long orderId;
    private String orderNumber;
    private Long totalPrice;
    private LocalDateTime cancellationDate; // 주문 취소 일자
    private List<ProductResponse> cancelledItems;
    private RefundInfo refundInfo;

    @Builder
    public OrderCancelReturnResponse(Long orderId, String orderNumber, Long totalPrice, LocalDateTime cancellationDate, RefundInfo refundInfo, List<ProductResponse> cancelledItems) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.cancellationDate = cancellationDate;
        this.refundInfo = refundInfo;
        this.cancelledItems = cancelledItems;
    }

    public static OrderCancelReturnResponse of(Order order, RefundDto refundDto) {
        return OrderCancelReturnResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNo())
                .totalPrice(order.getTotalPrice())
                .cancellationDate(order.getUpdatedAt())
                .refundInfo(RefundInfo.of(refundDto))
                .cancelledItems(
                        order.getOrderDetails().stream()
                                .map(orderDetail -> ProductResponse.of(orderDetail.getProduct(), orderDetail.getQuantity()))
                                .collect(Collectors.toList())
                )
                .build();

    }
}
