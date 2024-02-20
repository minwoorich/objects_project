package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewSurveyCategory;

import java.util.List;

public interface ReviewSurveyCategoryRepository {
    List<ReviewSurveyCategory> findAllByProductId(Long productId);

    ReviewSurveyCategory findByName(String name);
}
