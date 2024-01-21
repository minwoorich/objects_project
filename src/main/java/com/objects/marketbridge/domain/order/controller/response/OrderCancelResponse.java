package com.objects.marketbridge.domain.order.controller.response;

import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.entity.ProdOrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderCancelResponse {

    private List<ProductInfoResponse> productResponses;
    private RefundInfoResponse refundInfoResponse;

    @Builder
    private OrderCancelResponse(List<ProductInfoResponse> productResponses, RefundInfoResponse refundInfoResponse) {
        this.productResponses = productResponses;
        this.refundInfoResponse = refundInfoResponse;
    }

    public static OrderCancelResponse of(List<ProdOrderDetail> orderDetails, ProdOrder prodOrder) {
        return OrderCancelResponse.builder()
                .productResponses(orderDetails.stream()
                        .map(ProductInfoResponse::of)
                        .collect(Collectors.toList())
                )
                .refundInfoResponse(RefundInfoResponse.builder()
                        .refundFee(0)
                        .deliveryFee(0) // TODO 주문에서 배송비 가져오기
                        .discountPrice(prodOrder.getTotalUsedCouponPrice()) // TODO 할인금액 쿠폰만 가능?
                        .totalPrice(orderDetails.stream()
                                .mapToLong(ProdOrderDetail::getPrice)
                                .sum()
                        )
                        .build())
                .build();
    }

}
