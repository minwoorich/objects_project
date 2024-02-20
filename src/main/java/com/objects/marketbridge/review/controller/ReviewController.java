package com.objects.marketbridge.review.controller;

import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.review.dto.*;
import com.objects.marketbridge.review.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 서베이 선택창 조회
    @GetMapping("/review-survey-options/{productId}")
    @UserAuthorize
    public ApiResponse<List<ReviewSurveyQuestionAndOptionsDto>> getReviewSurveyQuestionAndOptionsList
        (@PathVariable("productId") Long productId){
        List<ReviewSurveyQuestionAndOptionsDto> response
                = reviewService.getReviewSurveyQuestionAndOptionsList(productId);
        return ApiResponse.ok(response);
    }



    //리뷰 등록
    @PostMapping("/review")
    public ApiResponse<ReviewIdDto> createReview
        (@Valid @RequestBody CreateReviewDto request, @AuthMemberId Long memberId) {
        Long reviewId = reviewService.createReview(request, memberId);
        ReviewIdDto response = new ReviewIdDto(reviewId);
        return ApiResponse.ok(response);
    }



    //리뷰아이디로 리뷰상세 단건 조회
    @GetMapping("/review/{reviewId}")
    public ApiResponse<ReviewSingleReadDto> getReview
        (@PathVariable("reviewId") Long reviewId, @AuthMemberId Long memberId) {
        ReviewSingleReadDto response = reviewService.getReview(reviewId, memberId);
        return ApiResponse.ok(response);
    }


//    //LIKE관련//
//    //상품별 리뷰 리스트 조회(createdAt 최신순 내림차순 정렬 또는 likes 많은순 내림차순 정렬)
//    //http://localhost:8080/product/1/reviews?page=0&sortBy=createdAt
//    //http://localhost:8080/product/1/reviews?page=0&sortBy=liked
//    @GetMapping("/product/{productId}/reviews")
//    public ApiResponse<Page<ReviewWholeInfoDto>> getProductReviews(
//            @PathVariable("productId") Long productId,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy) {
//
//        Pageable pageRequest = PageRequest.of(page, 5, Sort.by(sortBy).descending());
//        Page<ReviewWholeInfoDto> response = reviewService.getProductReviews(productId, pageRequest, sortBy);
//        return ApiResponse.ok(response);
//    }


//    //LIKE관련//
//    //회원별 리뷰 리스트 조회(createdAt 최신순 내림차순 정렬 또는 likes 많은순 내림차순 정렬)
//    //http://localhost:8080/member/1/reviews?page=0&sortBy=createdAt
//    //http://localhost:8080/member/1/reviews?page=0&sortBy=liked
//    @GetMapping("/member/{memberId}/reviews")
//    @UserAuthorize
//    public ApiResponse<Page<ReviewWholeInfoDto>> getMemberReviews(
//            @PathVariable("memberId") Long memberId,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy) {
//
//        Pageable pageRequest = PageRequest.of(page, 5, Sort.by(sortBy).descending());
//        Page<ReviewWholeInfoDto> response = reviewService.getMemberReviews(memberId, pageRequest, sortBy);
//        return ApiResponse.ok(response);
//    }



    //상품별 리뷰 총갯수 조회
    @GetMapping("/product/{productId}/reviews-count")
    public ApiResponse<ReviewsCountDto> getProductReviewsCount(@PathVariable("productId") Long productId){
        ReviewsCountDto reviewsCountDto = reviewService.getProductReviewsCount(productId);
        return ApiResponse.ok(reviewsCountDto);
    }



    //회원별 리뷰 총갯수 조회
    @GetMapping("/member/{memberId}/reviews-count")
    @UserAuthorize
    public ApiResponse<ReviewsCountDto> getMemberReviewsCount
        (@PathVariable("memberId") Long memberId){
        ReviewsCountDto reviewsCountDto = reviewService.getMemberReviewsCount(memberId);
        return ApiResponse.ok(reviewsCountDto);
    }



    //리뷰 수정
    @PatchMapping("/review/{reviewId}")
    public ApiResponse<ReviewIdDto> updateReview
        (@Valid @RequestBody ReviewModifiableValuesDto request,
         @PathVariable("reviewId") Long reviewId, @AuthMemberId Long memberId) {
        ReviewIdDto response = reviewService.updateReview(request, reviewId, memberId);
        return ApiResponse.ok(response);
    }



    //리뷰 삭제
    @DeleteMapping("/review/{reviewId}")
    public ApiResponse<ReviewIdDto> deleteReview
        (@PathVariable("reviewId") Long reviewId, @AuthMemberId Long memberId) {
        reviewService.deleteReview(reviewId, memberId);
        return ApiResponse.of(HttpStatus.OK);
    }



//    //LIKE관련//
//    //리뷰 좋아요 등록 또는 변경(True화/False화)
//    @PostMapping("/review/{reviewId}/like")
//    public ApiResponse<ReviewLikeDto> addOrChangeReviewLike(
//            @PathVariable("reviewId") Long reviewId,
//            @AuthMemberId Long memberId) {
//        ReviewLikeDto response = reviewService.addOrChangeReviewLike(reviewId, memberId);
//        return ApiResponse.ok(response);
//    }



//    //LIKE관련//
//    //리뷰 좋아요 총갯수 조회
//    @GetMapping("/review/{reviewId}/likes/count")
//    public ApiResponse<ReviewLikesCountDto> countReviewLikes
//    (@PathVariable("reviewId") Long reviewId) {
//        ReviewLikesCountDto response = reviewService.countReviewLikes(reviewId);
//        return ApiResponse.ok(response);
//    }
}
