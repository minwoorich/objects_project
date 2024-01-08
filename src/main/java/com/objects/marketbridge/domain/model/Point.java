package com.objects.marketbridge.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "point_id")
    private Long id;
    // TODO
    private Long userId;
    // TODO
    private Long orderId;

    private Integer inPoint;

    private Integer outPoint;

    private Integer balance;

    private String comments;

    @Builder
    private Point(Long userId, Long orderId, Integer inPoint, Integer outPoint, Integer balance, String comments) {
        this.userId = userId;
        this.orderId = orderId;
        this.inPoint = inPoint;
        this.outPoint = outPoint;
        this.balance = balance;
        this.comments = comments;
    }
}
