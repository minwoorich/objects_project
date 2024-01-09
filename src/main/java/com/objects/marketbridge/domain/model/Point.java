package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

//    private Long orderId;

    private Integer inPoint;

    private Integer outPoint;

    private Integer balance;

    private String comments;


    @Builder //order_id 제거
    private Point(User userId, Integer inPoint, Integer outPoint, Integer balance, String comments) {
        this.userId = userId;
        this.inPoint = inPoint;
        this.outPoint = outPoint;
        this.balance = balance;
        this.comments = comments;
    }
}
