package com.objects.marketbridge.member.dto;

import com.objects.marketbridge.member.controller.request.CreateSubsRequest;

import com.objects.marketbridge.payment.domain.Amount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateSubsDto {

    private String tid;
    private Long memberId;
    private String subsOrderNo;
    private Amount amout;

    @Builder
    public CreateSubsDto(String tid, Long memberId, String subsOrderNo, Amount amout) {
        this.tid = tid;
        this.memberId = memberId;
        this.subsOrderNo = subsOrderNo;
        this.amout = amout;
    }

    public static CreateSubsDto of(Amount amount, Long memberId, String tid, String subsOrderNo) {
        return CreateSubsDto.builder()
                .memberId(memberId)
                .subsOrderNo(subsOrderNo)
                .amout(amount)
                .tid(tid)
                .build();
    }
}

