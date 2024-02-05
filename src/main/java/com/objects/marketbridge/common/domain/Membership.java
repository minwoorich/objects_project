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
    @Column(name = "membership_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String cid;

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
    public Membership(Member member, String cid, String itemName, Long quantity, String tid, String sid, String subsOrderNo, String statusCode, String paymentMethod, CardInfo cardInfo, Amount amount) {
        this.member = member;
        this.cid = cid;
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

    public void update(Member member , String subsOrderNo,String tid, String sid, String cid, String itemName ,Long quantity, String paymentMethod, CardInfo cardInfo , Amount amount) {
           this.member =member;
           this.subsOrderNo=subsOrderNo;
           this.tid = tid;
           this.sid = sid;
           this.cid = cid;
           this.itemName = itemName;
           this.quantity = quantity;
           this.paymentMethod = paymentMethod;
           this.cardInfo = cardInfo;
           this.amount = amount;
    }
}
