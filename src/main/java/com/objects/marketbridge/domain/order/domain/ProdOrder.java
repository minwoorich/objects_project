package com.objects.marketbridge.domain.order.domain;

import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.BaseEntity;
import com.objects.marketbridge.domain.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProdOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private Long totalPrice;

    private Long pointRate; // 적립율

    private Long savedPoint;

    private LocalDateTime deliveredDate;

    @OneToMany(mappedBy = "prodOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdOrderDetail> prodOrderDetails = new ArrayList<>();

    @Builder
    private ProdOrder(Member member, Address address, Long totalPrice, Long pointRate, Long savedPoint, LocalDateTime deliveredDate, List<ProdOrderDetail> orderDetails) {
        this.member = member;
        this.address = address;
        this.totalPrice = totalPrice;
        this.pointRate = pointRate;
        this.savedPoint = savedPoint;
        this.deliveredDate = deliveredDate;
    }

    public void addOrderDetail(ProdOrderDetail prodOrderDetails) {
        this.prodOrderDetails.add(prodOrderDetails);
        prodOrderDetails.setOrder(this);
    }

    public static ProdOrder create(Member member, Address address, Long totalPrice){
        return ProdOrder.builder()
                .member(member)
                .address(address)
                .totalPrice(totalPrice)
                .build();
    }
}
