package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.Review;
import com.objects.marketbridge.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ReviewImageJpaRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findAllByReviewId(Long reviewId);

//    @Query("DELETE FROM ReviewImage ri WHERE ri IN :reviewImages")
//    void deleteAllByIdInBatch(List<Long> ids);
}
