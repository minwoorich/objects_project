package com.objects.marketbridge.review.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_content_id")
    private Long id;

    // TODO
    @Column(name = "survey_category_id")
    private Long reviewSurveyCategoryId;

    private String content;

    @Builder
    private SurveyContent(Long reviewSurveyCategoryId, String content) {
        this.reviewSurveyCategoryId = reviewSurveyCategoryId;
        this.content = content;
    }
}
