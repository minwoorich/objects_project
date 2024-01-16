package com.objects.marketbridge.domain.order.domain;

import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderTemp {
    @Column(name="order_temp_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String orderNo;

    Long amount;

    Long addressId;

    String orderName;

    String rewardType;

    String product;


    @Builder
    public OrderTemp(String orderNo, Long amount, Long addressId, String orderName, String rewardType, String product) {
        this.orderNo = orderNo;
        this.amount = amount;
        this.addressId = addressId;
        this.orderName = orderName;
        this.rewardType = rewardType;
        this.product = product;
    }
}
