package com.objects.marketbridge.review.service.port;

import com.objects.marketbridge.image.domain.Image;
import com.objects.marketbridge.review.domain.Review;
import com.objects.marketbridge.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ReviewImageRepository {

    void save(ReviewImage reviewImage);

    List<ReviewImage> findAllByReviewId(Long reviewId);

    void delete(ReviewImage reviewImage);

    void saveAll(List<ReviewImage> reviewImages);

    void deleteAllByIdInBatch(List<Long> ids);
}
