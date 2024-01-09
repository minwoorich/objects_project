package com.objects.marketbridge.domain.model;

import com.objects.marketbridge.domain.order.dto.OrderCreate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProdOrder extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "prod_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private Long totalPrice;

    private Integer pointRate; // 적립율

    private Integer savedPoint;

    private LocalDateTime deliveredDate;

    @Builder
    private ProdOrder(User user, Address address, Long totalPrice, Integer pointRate, Integer savedPoint, LocalDateTime deliveredDate) {
        this.user = user;
        this.address = address;
        this.totalPrice = totalPrice;
        this.pointRate = pointRate;
        this.savedPoint = savedPoint;
        this.deliveredDate = deliveredDate;
    }

//    public OrderCreate to() {
//        return OrderCreate.builder()
//                .
//    }
}
