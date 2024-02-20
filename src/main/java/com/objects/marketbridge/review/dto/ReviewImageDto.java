package com.objects.marketbridge.review.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImageDto {

    private Long seqNo;
    private String imgUrl;

    @Builder
    public ReviewImageDto(Long seqNo, String imgUrl) {
        this.seqNo = seqNo;
        this.imgUrl = imgUrl;
    }
}
