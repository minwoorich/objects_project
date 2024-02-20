package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepository {

    void save(Review review);

    Review findById(Long id);

    void delete(Review review);

//    //LIKE관련//
//    Page<Review> findAllByProductIdOrderByLikesDesc(Long productId, Pageable pageable);

//    //LIKE관련//
//    Page<Review> findAllByMemberIdOrderByLikesDesc(Long memberId, Pageable pageable);

    Page<Review> findAllByProductId(Long productId, Pageable pageable);

    Page<Review> findAllByMemberId(Long memberId, Pageable pageable);

    Long countByProductId(Long productId);

    Long countByMemberId(Long memberId);

    Page<Review> findAllByIdIn(List<Long> likedOrderedReviewIds, Pageable pageable);
}
