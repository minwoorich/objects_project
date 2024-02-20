package com.objects.marketbridge.review.dto;

import com.objects.marketbridge.review.domain.ReviewSurvey;
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

    private String productName;
    private String memberName;
    //    private String memberThumbnail; //썸네일은 없는것으로...
    private Integer rating;
    private List<ReviewSurvey> reviewSurveyList = new ArrayList<>();
    private String content;
    private List<String> reviewImgUrls = new ArrayList<>();
    private String sellerName; //Seller관련은 MarketBridge 단일로. 개인판매자는 일단 고려하지 않게.
//    private Long likes; //count된 like수. //LIKE관련//
    private LocalDateTime createdAt;

    @Builder

    public ReviewWholeInfoDto(String productName, String memberName, Integer rating,
                              List<ReviewSurvey> reviewSurveyList, String content,
                              List<String> reviewImgUrls, String sellerName, Long likes, LocalDateTime createdAt) {
        this.productName = productName;
        this.memberName = memberName;
        this.rating = rating;
        this.reviewSurveyList = reviewSurveyList;
        this.content = content;
        this.reviewImgUrls = reviewImgUrls;
        this.sellerName = sellerName;
//        this.likes = likes; //LIKE관련//
        this.createdAt = createdAt;
    }
}
