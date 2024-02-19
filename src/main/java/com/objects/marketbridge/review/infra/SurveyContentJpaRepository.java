package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.SurveyContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyContentJpaRepository extends JpaRepository<SurveyContent, Long> {

    List<SurveyContent> findAllBySurveyCategoryId(Long surveyCategoryId);

    Boolean existsBySurveyCategoryId(Long reviewSurveyCategoryid);
}
