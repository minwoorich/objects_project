package com.objects.marketbridge.member.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIdDto {
    private Long memberId;

    @Builder
    public MemberIdDto(Long memberId) {
        this.memberId = memberId;
    }
}
