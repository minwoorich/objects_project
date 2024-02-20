package com.objects.marketbridge.review.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.image.domain.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "review_id")
//    private Review review;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "image_id")
//    private Image image;

    private Long reviewId;
    private Long imageId;

    private Long seqNo;

    @Builder
    public ReviewImage(Long id, Long reviewId, Long imageId, Long seqNo) {
        this.id = id;
        this.reviewId = reviewId;
        this.imageId = imageId;
        this.seqNo = seqNo;
    }
}
