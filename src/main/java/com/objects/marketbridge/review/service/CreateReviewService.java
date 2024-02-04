package com.objects.marketbridge.review.service;

import com.objects.marketbridge.product.domain.Review;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.review.controller.request.CreateReviewRequestDto;
import com.objects.marketbridge.review.service.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateReviewService {

    private final ReviewRepository reviewRepository;
//    private final OrderDetailQueryRepository orderDetailQueryRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long create(CreateReviewRequestDto request){

        //리뷰는 상품결제후 배송완료/또는수취확인 된 상태에서만 작성가능하게 한다
        // 추후 로직 등록 TODO

        //리뷰 생성,저장
        Review review = Review.builder()
                .member(memberRepository.findById(request.getMemberId()))
                .product(productRepository.findById(request.getProductId()))
                .rating(request.getRating())
                .content(request.getContent())
                .build();
        reviewRepository.save(review);
        //리뷰id 반환
        return review.getId();
    }
}
