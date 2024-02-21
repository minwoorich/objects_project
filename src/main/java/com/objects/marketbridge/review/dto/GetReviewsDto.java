package com.objects.marketbridge.review.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReviewsDto {

    //구매자이름/구매자썸네일/평점/리뷰작성일/판매자명/상품명/옵션/리뷰이미지리스트/리뷰내용
    //리뷰설문내용/몇명에게도움
    //리스트, 페이지네이션 5개 단위
    private String memberName;
//    private String profileImg;
    private Integer rating;
    private ReviewProductDto reviewProduct;
    private List<GetReviewImageDto> reviewImages = new ArrayList<>();
    private List<GetReviewSurveyDto> reviewSurveys = new ArrayList<>();
    private String content;
    private Long likes; //count된 like수.
    private LocalDateTime createdAt;

    @Builder
    public GetReviewsDto(String memberName, Integer rating, ReviewProductDto reviewProduct, List<GetReviewImageDto> reviewImages, List<GetReviewSurveyDto> reviewSurveys, String content, Long likes, LocalDateTime createdAt) {
        this.memberName = memberName;
        this.rating = rating;
        this.reviewProduct = reviewProduct;
        this.reviewImages = reviewImages;
        this.reviewSurveys = reviewSurveys;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
    }
}
