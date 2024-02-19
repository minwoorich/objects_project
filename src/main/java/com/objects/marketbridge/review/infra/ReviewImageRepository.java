package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewImage;

import java.util.List;

public interface ReviewImageRepository {

    void save(ReviewImage reviewImage);

    List<ReviewImage> findAllByReviewId(Long reviewId);

    void delete(ReviewImage reviewImage);

    void saveAll(List<ReviewImage> reviewImages);
}
