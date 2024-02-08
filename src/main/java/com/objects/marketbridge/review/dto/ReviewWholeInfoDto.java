package com.objects.marketbridge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewWholeInfoDto {

    //구매자이름/구매자썸네일/평점/리뷰작성일/판매자명/상품명/옵션/리뷰이미지리스트/리뷰내용
    //리뷰설문내용/몇명에게도움
    //리스트, 페이지네이션 5개 단위

    private String memberName;
//    private String memberThumbnail; //썸네일은 없는것으로...
    private Integer rating;
    private LocalDateTime createdAt;
    private String sellerName; //Seller관련은 MarketBridge 단일로. 개인판매자는 일단 고려하지 않게.
    private String productName;
    private List<String> reviewImgUrls = new ArrayList<>();
    private String content;
//    private String reviewSurveyContent; //일단 생략
    private Long likes; //Review-like 엔티티 및 DB테이블 추가.

    @Builder
    public ReviewWholeInfoDto(String memberName,
                              Integer rating, LocalDateTime createdAt, String sellerName, String productName,
                              List<String> reviewImgUrls, String content,
                              Long likes) {
        this.memberName = memberName;
        this.rating = rating;
        this.createdAt = createdAt;
        this.sellerName = sellerName;
        this.productName = productName;
        this.reviewImgUrls = reviewImgUrls;
        this.content = content;
//        this.reviewSurveyContent = reviewSurveyContent;
        this.likes = likes;
    }
}
