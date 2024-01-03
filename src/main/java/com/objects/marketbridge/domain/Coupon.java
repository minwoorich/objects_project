package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    private Integer price;

    // TODO
    private Long productId;

    private Integer count;

    private Integer minimumPrice;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder
    private Coupon(String name, Integer price, Long productId, Integer count, Integer minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.count = count;
        this.minimumPrice = minimumPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
