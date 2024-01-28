package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.domain.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCancelReturnDetailResponse {

    private LocalDateTime orderDate;
    private LocalDateTime cancelDate;
    private String orderNo;
    private String cancelReason;
    private List<ProductResponse> productResponseList;
    private CancelRefundInfoResponse cancelRefundInfoResponse;


    @Builder
    private OrderCancelReturnDetailResponse(LocalDateTime orderDate, String orderNo, List<ProductResponse> productResponseList, LocalDateTime cancelDate, String cancelReason, CancelRefundInfoResponse cancelRefundInfoResponse) {
        this.orderDate = orderDate;
        this.orderNo = orderNo;
        this.productResponseList = productResponseList;
        this.cancelDate = cancelDate;
        this.cancelReason = cancelReason;
        this.cancelRefundInfoResponse = cancelRefundInfoResponse;
    }

    public static OrderCancelReturnDetailResponse of(Order order, List<OrderDetail> orderDetails, Payment payment) {
        return OrderCancelReturnDetailResponse.builder()
                .orderDate(order.getCreatedAt())
                .cancelDate(order.getUpdatedAt())
                .orderNo(order.getOrderNo())
                .cancelReason(orderDetails.get(0).getReason())
                .productResponseList(
                        orderDetails.stream()
                        .map(ProductResponse::of)
                        .toList()
                )
                .cancelRefundInfoResponse(
                        CancelRefundInfoResponse.builder()
                                .refundFee(0L)
                                .deliveryFee(0L)
                                .discountPrice(
                                        orderDetails.stream()
                                        .mapToLong(od -> od.getCoupon().getPrice())
                                        .sum()
                                )
                                .totalPrice(orderDetails.stream()
                                        .mapToLong(OrderDetail::getPrice)
                                        .sum()
                                )
                                .build()
                )
                .build();
    }

}
