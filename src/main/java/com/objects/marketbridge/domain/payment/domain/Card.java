package com.objects.marketbridge.domain.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    Long amount; // 카드사에 결제 요청한 금액
    String issuerCode; // 카드 발급사 숫자 코드
    String number; // 카드 번호(일부는 마스킹되어있음)
    Integer installmentPlanMonths; // 할부 개월 수(일시불=0)
    String approveNo; // 카드사 승인 번호
    Boolean useCardPoint; //카드사 포인트 사용 여부
    String cardType; // 신용, 체크, 기프트, 미확인(해외카드,간편결제)
    String ownerType; // 개인, 법인, 미확인(해외카드, 간편결제)
    String acquireStatus; // READY, REQUESTED, COMPLETED, CANCEL_REQUESTED, CANCELED
}
