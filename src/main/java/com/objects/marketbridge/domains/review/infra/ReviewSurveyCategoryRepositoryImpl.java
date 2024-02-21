package com.objects.marketbridge.domains.review.infra;

import com.objects.marketbridge.domains.review.domain.ReviewSurveyCategory;
import com.objects.marketbridge.domains.review.service.port.ReviewSurveyCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewSurveyCategoryRepositoryImpl implements ReviewSurveyCategoryRepository {

    private final ReviewSurveyCategoryJpaRepository reviewSurveyCategoryJpaRepository;

    @Override
    public List<ReviewSurveyCategory> findAllByProductId(Long productId) {
        return reviewSurveyCategoryJpaRepository.findAllByProductId(productId);
    }

    @Override
    public ReviewSurveyCategory findByName(String name) {
        return reviewSurveyCategoryJpaRepository.findByName(name);
    }
}
