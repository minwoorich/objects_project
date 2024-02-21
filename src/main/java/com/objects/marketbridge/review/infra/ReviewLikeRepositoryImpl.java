package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewLike;
import com.objects.marketbridge.review.service.port.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewLikeRepositoryImpl implements ReviewLikeRepository {

    private final ReviewLikeJpaRepository reviewLikeJpaRepository;

    @Override
    public Boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId) {
        return reviewLikeJpaRepository.existsByReviewIdAndMemberId(reviewId, memberId);
    }

    @Override
    public void save(ReviewLike reviewLike) {
        reviewLikeJpaRepository.save(reviewLike);
    }

    @Override
    public void deleteByReviewIdAndMemberId(Long reviewId, Long memberId) {
        reviewLikeJpaRepository.deleteByReviewIdAndMemberId(reviewId, memberId);
    }


//    @Override
//    public void deleteByReviewId(Long reviewId) {
//        reviewLikeJpaRepository.deleteByReviewId(reviewId);

//    }
//    @Override
//    public ReviewLikes findByReviewIdAndMemberId(Long reviewId, Long memberId) {
//        return reviewLikesJpaRepository.findByReviewIdAndMemberId(reviewId, memberId);

//    }
//
//    @Override
//    public Page<ReviewLikes> findAllByReview_Product_Id(Long productId, Pageable pageable) {
//        return reviewLikesJpaRepository.findAllByReview_Product_Id(productId, pageable);
//    }
//
//    @Override
//    public List<Long> findReviewIdsByProductIdOrderByLikedCount(Long productId) {
//        String sql = "SELECT r.review_id " +
//                "FROM review_likes rl " +
//                "JOIN review r ON rl.review_id = r.review_id " +
//                "WHERE r.product_id = :productId " +
//                "GROUP BY r.review_id " +
//                "ORDER BY COUNT(rl.review_id) DESC";
//        return em.createNativeQuery(sql)
//                .setParameter("productId", productId)
//                .getResultList();
//    }

//    @Override
//    public Page<ReviewLikes> findAllByReview_Member_Id(Long memberId, Pageable pageable) {
//        return reviewLikesJpaRepository.findAllByReview_Member_Id(memberId, pageable);
//    }
//
//    @Override
//    public void delete(ReviewLikes reviewLikes) {
//        reviewLikesJpaRepository.delete(reviewLikes);
//    }
//
//    @Override
//    public void deleteById(Long reviewLikesId) {
//        reviewLikesJpaRepository.deleteById(reviewLikesId);
//    }
//
//    @Override
//    public void deleteByReviewId(Long reviewId){
//        reviewLikesJpaRepository.deleteByReviewId(reviewId);
//    }
//
//    @Override
//    public Long countByReviewIdAndLikedIsTrue(Long reviewId){
//        return reviewLikesJpaRepository.countByReviewIdAndLikedIsTrue(reviewId);
//    }
//
//    @Override
//    public Boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId) {
//        return reviewLikesJpaRepository.existsByReviewIdAndMemberId(reviewId, memberId);
//    }
//
//    @Override
//    public List<ReviewLikes> findAllByReviewId(Long reviewId) {
//        return reviewLikesJpaRepository.findAllByReviewId(reviewId);
//    }
//
//    @Override
//    public void deleteAllByReviewId(Long reviewId) {
//        reviewLikesJpaRepository.deleteAllByReviewId(reviewId);
//    }
}
