package com.objects.marketbridge.domains.review.controller;

import com.objects.marketbridge.common.responseobj.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.domains.review.dto.*;
import com.objects.marketbridge.domains.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/surveys") //PathVariable -> QueryString으로. (완료)
    @UserAuthorize
    public ApiResponse<List<ReviewSurveyCategoryContentsDto>> getReviewSurveyCategoryContentsList
        (@RequestParam("productId") Long productId){
        List<ReviewSurveyCategoryContentsDto> response
                = reviewService.getReviewSurveyCategoryContentsList(productId);
        return ApiResponse.ok(response);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewIdDto> createReview
        (@Valid @RequestBody CreateReviewDto request, @AuthMemberId Long memberId) {
        reviewService.createReview(request, memberId);
        return ApiResponse.create();
    }

    @PatchMapping("")
    @UserAuthorize
    public ApiResponse<Void> updateReview(@RequestBody UpdateReviewDto request) {
        reviewService.updateReview(request);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{reviewId}")
    @UserAuthorize
    public ApiResponse<Void> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.ok(null);
    }

    //review_like upsert(없으면 create, 있으면 delete)
    @PostMapping("/{reviewId}/like")
    public ApiResponse<Void> upsertReviewLike
    (@PathVariable("reviewId") Long reviewId, @AuthMemberId Long memberId) {
        reviewService.upsertReviewLike(reviewId, memberId);
        return ApiResponse.ok(null);
    }

    //review_like 총갯수 조회
    @GetMapping("{reviewId}/like/count")
    public ApiResponse<ReviewLikeCountDto> countReviewLike
    (@PathVariable("reviewId") Long reviewId) {
        ReviewLikeCountDto response = reviewService.countReviewLike(reviewId);
        return ApiResponse.ok(response);
    }

    //상품의 리뷰 총갯수 조회
    @GetMapping("/count/product/{productId}")
    public ApiResponse<ReviewCountDto> getProductReviewCount
        (@PathVariable("productId") Long productId){
        ReviewCountDto reviewCountDto = reviewService.getProductReviewCount(productId);
        return ApiResponse.ok(reviewCountDto);
    }

    //멤버의 리뷰 총갯수 조회
    @GetMapping("/count/member/{memberId}")
    @UserAuthorize
    public ApiResponse<ReviewCountDto> getMemberReviewsCount
    (@PathVariable("memberId") Long memberId){
        ReviewCountDto reviewCountDto = reviewService.getMemberReviewCount(memberId);
        return ApiResponse.ok(reviewCountDto);
    }

    //리뷰아이디로 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    @UserAuthorize
    public ApiResponse<GetReviewDto> getReview
    (@PathVariable("reviewId") Long reviewId) {
        GetReviewDto getReviewDto = reviewService.getReview(reviewId);
        return ApiResponse.ok(getReviewDto);
    }

    //멤버의 리뷰 리스트 조회(createdAt: 최신순 내림차순 정렬 / likes: 좋아요 많은순 내림차순 정렬
    //http://localhost:8080/review/reviews/member/1?page=0&sortBy=createdAt
    //http://localhost:8080/review/reviews/member/1?reviews?page=0&sortBy=likes
    @GetMapping("/reviews/member/{memberId}") // /review/written
    @UserAuthorize
    public ApiResponse<Page<GetReviewDto>> getReviewsOfMember(
            @PathVariable("memberId") Long memberId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy) {

        int springPage = page-1;
        Pageable pageRequest = PageRequest.of(springPage, 5, Sort.by(sortBy).descending());
        Page<GetReviewDto> response = reviewService.getReviewsOfMember(memberId, pageRequest, sortBy);
        return ApiResponse.ok(response);
    }

    //상품의 리뷰 리스트 조회(createdAt: 최신순 내림차순 정렬 / likes: 좋아요 많은순 내림차순 정렬
    //http://localhost:8080/review/reviews/product/1?page=0&sortBy=createdAt
    //http://localhost:8080/review/reviews/product/1?reviews?page=0&sortBy=likes
    @GetMapping("/reviews/product/{productId}")
    public ApiResponse<Page<GetReviewDto>> getReviewsOfProduct(
            @PathVariable("productId") Long productId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy) {

        int springPage = page-1;
        Pageable pageRequest = PageRequest.of(springPage, 5, Sort.by(sortBy).descending());
        Page<GetReviewDto> response = reviewService.getReviewsOfProduct(productId, pageRequest, sortBy);
        return ApiResponse.ok(response);
    }




    //TODO: 리뷰리스트 조회, 리뷰조회, 상품별 리뷰 갯수, 리뷰 좋아요, mypage에 작성할 리뷰, 작성한 리뷰 리스트

    //회원의 리뷰 작성하지 않은 주문상세/상품 리스트 조회




    //회원의 리뷰 작성한 주문상세/상품 리스트 조회 (작성한 리뷰 리스트로 대체?)




    //작성된 review_survey 단건 조회 (리뷰아이디로 리뷰 단건 조회에 넣어놨기는 함)



}
