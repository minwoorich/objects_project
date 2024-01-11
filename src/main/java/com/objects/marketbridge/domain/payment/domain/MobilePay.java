package com.objects.marketbridge.domain.payment.domain;

import lombok.Getter;

@Getter
public class MobilePay {
    private String customerMobilePhone; // 결제에 사용한 폰번호
    private String settlementStatus; // 정산 상태 INCOMPLETED, COMPLETED
}
