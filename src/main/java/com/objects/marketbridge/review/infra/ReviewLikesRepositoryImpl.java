package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewLikes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewLikesRepositoryImpl implements ReviewLikesRepository{

    private final ReviewLikesJpaRepository reviewLikesJpaRepository;

    @Override
    public ReviewLikes findByReviewId(Long reviewId) {
        return reviewLikesJpaRepository.findByReviewId(reviewId);
    }

    @Override
    public void save(ReviewLikes reviewLikes) {
        reviewLikesJpaRepository.save(reviewLikes);
    }

    @Override
    public Page<ReviewLikes> findAllByReview_Product_Id(Long productId, Pageable pageable) {
        return reviewLikesJpaRepository.findAllByReview_Product_Id(productId, pageable);
    }

    @Override
    public Page<ReviewLikes> findAllByReview_Member_Id(Long memberId, Pageable pageable) {
        return reviewLikesJpaRepository.findAllByReview_Member_Id(memberId, pageable);
    }
}
