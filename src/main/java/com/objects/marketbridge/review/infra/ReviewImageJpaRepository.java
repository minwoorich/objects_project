package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.image.domain.Image;
import com.objects.marketbridge.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageJpaRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findAllByReviewId(Long reviewId);
    void deleteAll(Iterable<? extends ReviewImage> reviewImages);

    @Modifying
    @Query("DELETE FROM ReviewImage ri WHERE ri.reviewId = :reviewId")
    void deleteByReviewId(@Param("reviewId") Long reviewId);
}
