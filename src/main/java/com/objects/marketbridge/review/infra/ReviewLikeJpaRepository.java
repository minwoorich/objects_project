package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewLikeJpaRepository extends JpaRepository<ReviewLike, Long> {
    Boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId);

    void deleteByReviewIdAndMemberId(Long reviewId, Long memberId);

//    @Modifying
//    @Query("DELETE FROM ReviewLike rl WHERE rl.reviewId = :reviewId")
//    void deleteByReviewId(@Param("reviewId") Long reviewId);

//    ReviewLike findByReviewIdAndMemberId(Long reviewId, Long memberId);
//
//    Page<ReviewLike> findAllByReview_Product_Id(Long productId, Pageable pageable);
//
//    Page<ReviewLike> findAllByReview_Member_Id(Long memberId, Pageable pageable);
//
//    void delete(ReviewLike reviewLike);
//
//    void deleteById(Long reviewLikesId);
//
//    void deleteByReviewId(Long reviewId);
//
//    Long countByReviewIdAndLikedIsTrue(Long reviewId);
//
//    Boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId);
//
//    List<ReviewLike> findAllByReviewId(Long reviewId);
//
//    void deleteAllByReviewId(Long reviewId);
}
