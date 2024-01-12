package com.objects.marketbridge.domain.payment.domain;

import lombok.Getter;

@Getter
public class VrAccount {
    private String accountType; // 일반, 고정
    private String accountNumber; // 발급된 계좌번호
    private String bankCode; // 가상계좌 은행 숫자 코드
    private String cutomerName; // 가상계좌를 발급한 고객 이름
    private String dueDate; // 입금 기한 yyyy-MM-dd'T'HH:mm:ss (ISO8601)
    private String refundStatus; // NONE, PENDING, FAILED, PARTIAL_FAILED, COMPLETED
    private Boolean expired; //가상계좌 만료 여부
    private String settlementStatus; // 정상상태, INCOMPLETED, COMPLETED
    private RefundReceiveAccount refundAccount; // 환불 받을 계좌 정보

    static class RefundReceiveAccount {
        private String bankCode; // 환불받을
        private String accountNumber;
        private String holderName;
    }
}
