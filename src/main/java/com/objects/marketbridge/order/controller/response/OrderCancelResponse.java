package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderCancelResponse {

    private List<ProductInfoResponse> productResponses;
    private CancelRefundInfoResponse cancelRefundInfoResponse;

    @Builder
    private OrderCancelResponse(List<ProductInfoResponse> productResponses, CancelRefundInfoResponse cancelRefundInfoResponse) {
        this.productResponses = productResponses;
        this.cancelRefundInfoResponse = cancelRefundInfoResponse;
    }

    public static OrderCancelResponse of(List<OrderDetail> orderDetails, Order order) {
        return OrderCancelResponse.builder()
                .productResponses(orderDetails.stream()
                        .map(ProductInfoResponse::of)
                        .collect(Collectors.toList())
                )
                .cancelRefundInfoResponse(CancelRefundInfoResponse.builder()
                        .refundFee(0L)
                        .deliveryFee(0L) // TODO 주문에서 배송비 가져오기
                        .discountPrice(order.getTotalUsedCouponPrice()) // TODO 할인금액 쿠폰만 가능?
                        .totalPrice(orderDetails.stream()
                                .mapToLong(OrderDetail::getPrice)
                                .sum()
                        )
                        .build())
                .build();
    }

}
