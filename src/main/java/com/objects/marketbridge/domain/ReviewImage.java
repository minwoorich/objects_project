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
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    // TODO
    private Long reviewId;
    // TODO
    private Long imageId;

    @Builder
    private ReviewImage(Long reviewId, Long imageId) {
        this.reviewId = reviewId;
        this.imageId = imageId;
    }
}
