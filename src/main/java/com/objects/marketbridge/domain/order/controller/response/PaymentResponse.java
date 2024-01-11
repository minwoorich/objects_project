package com.objects.marketbridge.domain.order.controller.response;

import com.objects.marketbridge.domain.payment.domain.Card;
import com.objects.marketbridge.domain.payment.domain.MobilePay;
import com.objects.marketbridge.domain.payment.domain.Transfer;
import com.objects.marketbridge.domain.payment.domain.VrAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentKey;
    private String paymentType; // toss 의 경우 일반결제, 자동결제, 브랜드페이로 나뉨
    private String orderId;
    private String orderName; // ex) 생수 외 1건
    private String payMethod; // 카드, 가상계좌, 간편결제, 휴대폰, 계좌이체
    private Integer totalAmount; // 총 결제 금액, 결제 상태가 변해도 최초에 결제된 금액으로 유지
    private Integer balanceAmount; // 결제 취소되고 나서 남은 값
    private String requestedAt; // 결제가 일어난 날짜와 시간정보, yyyy-MM-dd'T'HH:mm:ss±hh:mm (ISO 8601)
    private String approvedAt; // 결제 승인이 일어난 날자와 시간정보, yyyy-MM-dd'T'HH:mm:ss±hh:mm (ISO 8601)
    //    Boolean useEscrow; // 에스크로(안전결제) 사용여부, 현금결제시 필수 (but, 돈내야함 ㅠ)
    private List<Object> cancels; // [cancelAmount(취소한 금액), cancelReason(취소사유), refundableAmount(환불가능한 잔액), easyPayDiscountAmount(적립식 결제수단 ex,네이버페이의 포인트사용), canceledAt(취소 날짜시간, ISO8601), transactionKey(취소 건의 키값), receiptKey(취소건의 현금영수증 키값)]
    private Boolean isPartialCancelable; //부분 취소 가능 여부 (false면 전액 취소만 가능)

    // 결제방식 별 추가 반환정보
    private Card card; // 카드 결제시 제공되는 정보
    private VrAccount vrAccount; // 가상계좌로 결제시 제공되는 정보
    private MobilePay mobilePay; // 휴대폰 결제시 제공되는 정보
    private Transfer transfer; // 계좌이체로 결제시 제공되는 정보

    // 영수증 정보
    private String receiptUrl; // 영수증 정보를 확인할수 있는 url
    private String checkoutUrl; // 결제창이 열리는 주소


    // 현금 영수증 : 테스트용 API 에선 제공 X




}
