package com.objects.marketbridge.review.service;

import com.objects.marketbridge.common.domain.Review;
import com.objects.marketbridge.review.controller.response.ReadReviewResponseDto;
import com.objects.marketbridge.review.service.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReadReviewResponseDto read(Long reviewId){
        //orderDetailId와 orderDetailStatusCode "없는" 상태로 응답값 주게 작성함.
        //필요시 수정 요함. TODO
        Review findReview = reviewRepository.findById(reviewId);
        ReadReviewResponseDto readReviewResponseDto
                = ReadReviewResponseDto.builder()
                .memberId(findReview.getMember().getId())
                .productId(findReview.getProduct().getId())
                .rating(findReview.getRating())
                .content(findReview.getContent())
                .build();
        return readReviewResponseDto;
    }
}
