package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.SurveyContent;
import com.objects.marketbridge.review.service.port.SurveyContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyContentRepositoryImpl implements SurveyContentRepository {

    private final SurveyContentJpaRepository surveyContentJpaRepository;

    @Override
    public List<SurveyContent> findAllBySurveyCategoryId(Long surveyCategoryId) {
        return surveyContentJpaRepository.findAllBySurveyCategoryId(surveyCategoryId);
    }

    @Override
    public Boolean existsBySurveyCategoryId(Long reviewSurveyCategoryid) {
        return surveyContentJpaRepository.existsBySurveyCategoryId(reviewSurveyCategoryid);
    }
}
