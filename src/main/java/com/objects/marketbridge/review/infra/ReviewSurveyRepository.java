package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewSurvey;
import com.objects.marketbridge.review.domain.ReviewSurveyCategory;

import java.util.List;

public interface ReviewSurveyRepository {

    ReviewSurvey save(ReviewSurvey reviewSurvey);

    void deleteAllByReviewId(Long reviewId);

    List<ReviewSurvey> findAllByReviewId(Long reviewId);
}
