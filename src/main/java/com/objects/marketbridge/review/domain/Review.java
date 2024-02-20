package com.objects.marketbridge.review.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImages = new ArrayList<>();

    // 별점
    private Integer rating; //1-5

    private String content;

    private String summary;

//    //LIKE관련//

//    private Long likes = 0L;

    @Builder
    public Review(Member member, Product product, List<ReviewImage> reviewImages,
                  Integer rating, String content, String summary) {
        this.member = member;
        this.product = product;
        this.reviewImages = reviewImages;
        this.rating = rating;
        this.content = content;
        this.summary = summary;
    }

    public void update(List<ReviewImage> reviewImages, Integer rating, String content, String summary) {
        this.reviewImages = reviewImages;
        this.rating = rating;
        this.content = content;
        this.summary = summary;
    }

//    //LIKE관련//
//    public void increaseLikes(){
//        this.likes++;
//    }
//    public void decreaseLikes(){
//        this.likes--;
//    }
}
