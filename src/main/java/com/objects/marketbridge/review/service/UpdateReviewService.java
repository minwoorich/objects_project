package com.objects.marketbridge.review.service;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.review.domain.Review;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.review.controller.request.UpdateReviewRequestDto;
import com.objects.marketbridge.review.controller.response.UpdateReviewResponseDto;
import com.objects.marketbridge.review.service.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UpdateReviewResponseDto update(Long reviewId, UpdateReviewRequestDto request){
        //orderDetailId와 orderDetailStatusCode "없는" 상태로 응답값 주게 작성함.
        //필요시 수정 요함. TODO
        Review findReview = reviewRepository.findById(reviewId);

        Member member = memberRepository.findById(request.getMemberId());
        Product product = productRepository.findById(request.getProductId());
        Integer rating = findReview.getRating();
        String content = findReview.getContent();

        findReview.update(member, product, rating, content);
        reviewRepository.save(findReview);

        UpdateReviewResponseDto response = UpdateReviewResponseDto.builder()
                .reviewId(findReview.getId())
                .memberId(member.getId())
                .productId(product.getId())
                .rating(rating)
                .content(content)
                .build();
        return response;
    }
}
