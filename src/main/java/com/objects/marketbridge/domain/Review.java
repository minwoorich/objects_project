package com.objects.marketbridge.domain;

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
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    // TODO
    private Long orderId;
    // TODO
    private Long productId;

    private String content;
    // 별점
    private Integer rating; //1-5

    @Builder
    private Review(Long orderId, Long productId, String content, Integer rating) {
        this.orderId = orderId;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
    }
}
