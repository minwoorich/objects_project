package com.objects.marketbridge.common.domain;

import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subs_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member Member;

    private String cid;
    @JoinColumn(name = "partner_order_id")
    private String partnerOrderId;

    @JoinColumn(name = "partner_user_id")
    private String partnerUserId;

    @JoinColumn(name = "item_name")
    private String itemName;

    private Long quantity;

    private String tid;

    private String sid;

    private String subsOrderNo;

    private String statusCode;

    private String paymentMethod; // CARD, MONEY
    // 카드 결제
    @Embedded
    private CardInfo cardInfo;

    // 결제 금액 정보
    @Embedded
    private Amount amount;

    @Builder
    public Membership(com.objects.marketbridge.common.domain.Member member, String cid, String partnerOrderId, String partnerUserId, String itemName, Long quantity, String tid, String sid, String subsOrderNo, String statusCode, String paymentMethod, CardInfo cardInfo, Amount amount) {
        Member = member;
        this.cid = cid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.tid = tid;
        this.sid = sid;
        this.subsOrderNo = subsOrderNo;
        this.statusCode = statusCode;
        this.paymentMethod = paymentMethod;
        this.cardInfo = cardInfo;
        this.amount = amount;
    }

    public static Membership create(Member member, String subsOrderNo, String tid, Amount amount){
        return Membership.builder()
                .member(member)
                .subsOrderNo(subsOrderNo)
                .amount(amount)
                .tid(tid)
                .build();
    }
    public static Membership createPayment(String tid, String sid, String cid, String partnerOrderId,String partnerUserId, String itemName ,Long quantity, String paymentMethod, CardInfo cardInfo , Amount amount) {
        return Membership.builder()
                .tid(tid)
                .sid(sid)
                .cid(cid)
                .subsOrderNo(partnerOrderId)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .itemName(itemName)
                .quantity(quantity)
                .paymentMethod(paymentMethod)
                .cardInfo(cardInfo)
                .amount(amount)
                .build();
    }
}
