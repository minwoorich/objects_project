package com.objects.marketbridge.review.controller;

import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.review.dto.CreateReviewDto;
import com.objects.marketbridge.review.dto.ReviewAllValuesDto;
import com.objects.marketbridge.review.dto.ReviewIdDto;
import com.objects.marketbridge.review.dto.ReviewModifiableValuesDto;
import com.objects.marketbridge.review.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 등록
    @UserAuthorize
    @PostMapping
    public ApiResponse<ReviewIdDto> createReview
        (@Valid @RequestBody CreateReviewDto request, @AuthMemberId Long memberId) {
        Long reviewId = reviewService.createReview(request, memberId);
        ReviewIdDto response = new ReviewIdDto(reviewId);
        return ApiResponse.ok(response);
    }

//    //리뷰 조회
//    @UserAuthorize
//    @GetMapping("/{reviewId}")
//    public ApiResponse<ReviewAllValuesDto> getReview
//        (@PathVariable("reviewId") Long reviewId, @AuthMemberId Long memberId) {
//        ReviewAllValuesDto response = reviewService.getReview(reviewId, memberId);
//        return ApiResponse.ok(response);
//    }
//
//    //리뷰 수정
//    @UserAuthorize
//    @PatchMapping("/{reviewId}")
//    public ApiResponse<ReviewIdDto> updateReview
//        (@Valid @RequestBody ReviewModifiableValuesDto request,
//         @PathVariable("reviewId") Long reviewId, @AuthMemberId Long memberId) {
//        ReviewIdDto response = reviewService.updateReview(request, reviewId, memberId);
//        return ApiResponse.ok(response);
//    }
//
//    //리뷰 삭제
//    @UserAuthorize
//    @DeleteMapping("/{reviewId}")
//    public ApiResponse<ReviewIdDto> deleteReview
//        (@PathVariable("reviewId") Long reviewId, @AuthMemberId Long memberId) {
//        reviewService.deleteReview(reviewId, memberId);
//        return ApiResponse.of(HttpStatus.OK);
//    }
}
