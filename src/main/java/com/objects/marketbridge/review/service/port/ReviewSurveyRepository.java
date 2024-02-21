package com.objects.marketbridge.review.service.port;

import com.objects.marketbridge.review.domain.ReviewSurvey;
import com.objects.marketbridge.review.domain.ReviewSurveyCategory;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewSurveyRepository {

    ReviewSurvey save(ReviewSurvey reviewSurvey);

    void deleteAllByReviewId(Long reviewId);

    List<ReviewSurvey> findAllByReviewId(Long reviewId);

//    void deleteByReviewId(Long reviewId);
}
