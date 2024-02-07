package com.objects.marketbridge.review.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Long orderDetailId;

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImages = new ArrayList<>();

    // 별점
    private Integer rating; //1-5

    private String content;

    @Builder
    public Review(Member member, Product product, Long orderDetailId, List<ReviewImage> reviewImages, Integer rating, String content) {
        this.member = member;
        this.product = product;
        this.orderDetailId = orderDetailId;
        this.reviewImages = reviewImages;
        this.rating = rating;
        this.content = content;
    }

    public void update(List<ReviewImage> reviewImages, Integer rating, String content) {
        this.reviewImages = reviewImages;
        this.rating = rating;
        this.content = content;
    }
}
