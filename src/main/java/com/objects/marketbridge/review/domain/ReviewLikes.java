//package com.objects.marketbridge.review.domain;
//
//import com.objects.marketbridge.member.domain.Member;
//import com.objects.marketbridge.product.domain.Product;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class ReviewLikes {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "review_likes_id")
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "review_id")
//    private Review review;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
//
////    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "product_id")
////    private Product product;
//
//    private Boolean liked;
//
//    @Builder
//    public ReviewLikes(Review review, Member member,
////                       Product product,
//                       Boolean liked) {
//        this.review = review;
//        this.member = member;
////        this.product = product;
//        this.liked = liked;
//    }
//
//public Boolean changeLiked(){
//    this.liked = !liked;
//    return this.liked;
//    }
//}
////LIKE관련//
