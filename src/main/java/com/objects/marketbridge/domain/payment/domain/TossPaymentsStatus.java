package com.objects.marketbridge.domain.payment.domain;

public enum TossPaymentsStatus {

    // 결제를 생성하면 가지게 되는 초기 상태 (인증 전까진 READY 상태를 유지한다)
    READY,

    // 결제수단 정보와 해당 결제수단의 소유자가 맞는지 인증을 마친 상태, 결제승인 API를 호출하면 결제가 완료됨
    IN_PROGRESS,

    // 가상계좌 결제 흐름에만 있는 상태, 결제 고객이 발급된 가상계좌에 입금하는 것을 기다리고 있는 상태
    WAITING_FOR_DEPOSIT,

    // 인증된 결제수단 정보, 고객 정보로 요청한 결제가 승인된 상태
    DONE,

    // 승인된 결제가 취소된 상태
    CANCELED,

    // 승인된 결제가 부분 취소 된 상태
    PARTIAL_CANCELED,

    // 결제 승인이 실패한 상태
    APORTED,

    // 결제 유효 시간 30분이 지나 거래가 취소된 상태, (IN_PROGRESS 에서 결제 승인 API를 호출하지 않으면 EXPIRED 됨)
    EXPIRED
}
