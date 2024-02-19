package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewSurveyCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewSurveyCategoryRepositoryImpl implements ReviewSurveyCategoryRepository{

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
