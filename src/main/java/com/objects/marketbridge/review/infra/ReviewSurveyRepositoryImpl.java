package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewSurvey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewSurveyRepositoryImpl implements ReviewSurveyRepository{

    private final ReviewSurveyJpaRepository reviewSurveyJpaRepository;

    @Override
    public ReviewSurvey save(ReviewSurvey reviewSurvey) {
        return reviewSurveyJpaRepository.save(reviewSurvey);
    }

    @Override
    public void deleteAllByReviewId(Long reviewId) {
        reviewSurveyJpaRepository.deleteAllByReviewId(reviewId);
    }

    @Override
    public List<ReviewSurvey> findAllByReviewId(Long reviewId) {
        return reviewSurveyJpaRepository.findAllByReviewId(reviewId);
    }
}
