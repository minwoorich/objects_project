package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCancelReturn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_cancel_return_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    private String statusCode;

    private Long quantity;

    private String reason;

    private Long refundAmount;

    @Builder
    private OrderCancelReturn(OrderDetail orderDetail, String statusCode, Long quantity, String reason, Long refundAmount) {
        this.orderDetail = orderDetail;
        this.statusCode = statusCode;
        this.quantity = quantity;
        this.reason = reason;
        this.refundAmount = refundAmount;
    }

    public static OrderCancelReturn create(OrderDetail orderDetail, String statusCode, String reason) {
        return OrderCancelReturn.builder()
                .orderDetail(orderDetail)
                .quantity(orderDetail.getReducedQuantity())
                .reason(reason)
                .statusCode(statusCode)
                .refundAmount(Long.valueOf(orderDetail.cancelAmount()))
                .build();
    }
}
