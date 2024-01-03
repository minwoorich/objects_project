package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyContent extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "survey_content_id")
    private Long id;

    // TODO
    private Long surveyCategoryId;

    private Integer seqNo; // 1,2,3

    private String content;

    @Builder
    private SurveyContent(Long surveyCategoryId, Integer seqNo, String content) {
        this.surveyCategoryId = surveyCategoryId;
        this.seqNo = seqNo;
        this.content = content;
    }
}
