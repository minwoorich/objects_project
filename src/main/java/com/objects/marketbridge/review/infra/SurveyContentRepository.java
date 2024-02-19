package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.SurveyContent;

import java.util.List;

public interface SurveyContentRepository {

    List<SurveyContent> findAllBySurveyCategoryId(Long surveyCategoryId);

    Boolean existsBySurveyCategoryId(Long reviewSurveyCategoryid);
}
