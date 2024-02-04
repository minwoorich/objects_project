package com.objects.marketbridge.product.domain;

import com.objects.marketbridge.common.domain.BaseEntity;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    //private Long orderId; orderid 제거

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 별점
    private Integer rating; //1-5

    private String content;


    @Builder
    private Review(Member member, Product product, Integer rating, String content) {
        this.member = member;
        this.product = product;
        this.rating = rating;
        this.content = content;
    }

//    public void create(Member member, Product product, Integer rating, String content){
//        this.member = member;
//        this.product = product;
//        this.rating = rating;
//        this.content = content;
//    }

    public void update(Member member, Product product, Integer rating, String content){
        this.member = member;
        this.product = product;
        this.rating = rating;
        this.content = content;
    }
}
