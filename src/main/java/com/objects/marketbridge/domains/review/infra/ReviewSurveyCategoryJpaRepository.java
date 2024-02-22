package com.objects.marketbridge.domains.review.infra;

import com.objects.marketbridge.domains.review.domain.ReviewSurveyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSurveyCategoryJpaRepository extends JpaRepository<ReviewSurveyCategory, Long> {

    List<ReviewSurveyCategory> findAllByProductId(Long productId);

    ReviewSurveyCategory findByName(String name);
}
