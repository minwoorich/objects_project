package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewLikes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikesJpaRepository extends JpaRepository<ReviewLikes, Long> {

    ReviewLikes findByReviewId(Long reviewId);

    Page<ReviewLikes> findAllByReview_Product_Id(Long productId, Pageable pageable);

    Page<ReviewLikes> findAllByReview_Member_Id(Long memberId, Pageable pageable);

    void delete(ReviewLikes reviewLikes);

    void deleteById(Long reviewLikesId);
}
