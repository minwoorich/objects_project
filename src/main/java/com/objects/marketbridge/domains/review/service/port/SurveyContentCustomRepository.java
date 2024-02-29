package com.objects.marketbridge.domains.review.service.port;

import com.objects.marketbridge.domains.review.domain.SurveyContent;
import com.objects.marketbridge.domains.review.dto.ReviewSurveyCategoryContentsDto;

import java.util.List;

public interface SurveyContentCustomRepository {

    List<ReviewSurveyCategoryContentsDto> findAllByProductIdWithContent(Long productId);
}
