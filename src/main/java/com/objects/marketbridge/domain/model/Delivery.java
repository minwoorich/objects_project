package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // NORMAL, EXCHANGE, RETURN // column이름이랑 다름

    // TODO
    private Long sellerId;
    // TODO
    private Long addressId;
    // TODO
    private Long orderDetailId;

    private String carrier;

    private String trackingNo;

    private String status;

    private LocalDateTime shipDate;

    private LocalDateTime deliveredDate;

    @Builder
    private Delivery(DeliveryType deliveryType, Long sellerId, Long addressId, Long orderDetailId, String carrier, String trackingNo, String status, LocalDateTime shipDate, LocalDateTime deliveredDate) {
        this.deliveryType = deliveryType;
        this.sellerId = sellerId;
        this.addressId = addressId;
        this.orderDetailId = orderDetailId;
        this.carrier = carrier;
        this.trackingNo = trackingNo;
        this.status = status;
        this.shipDate = shipDate;
        this.deliveredDate = deliveredDate;
    }
}
