package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSurveyJpaRepository extends JpaRepository<ReviewSurvey, Long> {

    ReviewSurvey save(ReviewSurvey reviewSurvey);

    void deleteAllByReviewId(Long reviewId);

    List<ReviewSurvey> findAllByReviewId(Long reviewId);
}
