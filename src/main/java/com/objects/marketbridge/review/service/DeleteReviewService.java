package com.objects.marketbridge.review.service;

import com.objects.marketbridge.common.domain.Review;
import com.objects.marketbridge.review.controller.request.DeleteReviewRequestDto;
import com.objects.marketbridge.review.controller.response.DeleteReviewResponseDto;
import com.objects.marketbridge.review.service.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeleteReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public DeleteReviewResponseDto delete(Long reviewId, DeleteReviewRequestDto request){
        Review findReview = reviewRepository.findById(reviewId);
        reviewRepository.delete(findReview);

        DeleteReviewResponseDto response = DeleteReviewResponseDto.builder()
                .reviewId(reviewId)
                .build();
        return  response;
    }
}
