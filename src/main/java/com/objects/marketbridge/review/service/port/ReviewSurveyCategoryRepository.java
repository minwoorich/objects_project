package com.objects.marketbridge.review.service.port;

import com.objects.marketbridge.review.domain.ReviewSurveyCategory;

import java.util.List;

public interface ReviewSurveyCategoryRepository {
    List<ReviewSurveyCategory> findAllByProductId(Long productId);

    ReviewSurveyCategory findByName(String name);
}
