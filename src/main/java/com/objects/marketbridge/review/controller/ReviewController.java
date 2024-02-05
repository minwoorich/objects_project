package com.objects.marketbridge.review.controller;

import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.review.controller.request.CreateReviewRequestDto;
import com.objects.marketbridge.review.controller.request.DeleteReviewRequestDto;
import com.objects.marketbridge.review.controller.request.UpdateReviewRequestDto;
import com.objects.marketbridge.review.controller.response.CreateReviewResponseDto;
import com.objects.marketbridge.review.controller.response.DeleteReviewResponseDto;
import com.objects.marketbridge.review.controller.response.ReadReviewResponseDto;
import com.objects.marketbridge.review.controller.response.UpdateReviewResponseDto;
import com.objects.marketbridge.review.service.CreateReviewService;
import com.objects.marketbridge.review.service.DeleteReviewService;
import com.objects.marketbridge.review.service.ReadReviewService;
import com.objects.marketbridge.review.service.UpdateReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewService createReviewService;
    private final ReadReviewService readReviewService;
    private final UpdateReviewService updateReviewService;
    private final DeleteReviewService deleteReviewService;

    //리뷰 등록
    @UserAuthorize
    @PostMapping("/reviews")
    public ApiResponse<CreateReviewResponseDto> createReview(@Valid @RequestBody CreateReviewRequestDto request) {
        Long reviewId = createReviewService.create(request);
        CreateReviewResponseDto response = new CreateReviewResponseDto(reviewId);
        return ApiResponse.ok(response);
    }

    //리뷰 조회
    @UserAuthorize
    @GetMapping("/reviews/{id}")
    public ApiResponse<ReadReviewResponseDto> readReview(@PathVariable("id") Long id) {
        ReadReviewResponseDto response = readReviewService.read(id);
        return ApiResponse.ok(response);
    }

    //리뷰 수정
    @UserAuthorize
    @PatchMapping("/reviews/{id}")
    public ApiResponse<UpdateReviewResponseDto> updateReview
        (@PathVariable("id") Long id, @Valid @RequestBody UpdateReviewRequestDto request) {
        UpdateReviewResponseDto response = updateReviewService.update(id, request);
        return ApiResponse.ok(response);
    }

    //리뷰 삭제
    @UserAuthorize
    @DeleteMapping("/reviews/{id}")
    public ApiResponse<DeleteReviewResponseDto> deleteReview
        (@PathVariable("id") Long id, @Valid @RequestBody DeleteReviewRequestDto request) {

        DeleteReviewResponseDto response = deleteReviewService.delete(id, request);
        return ApiResponse.ok(response);
    }
}
