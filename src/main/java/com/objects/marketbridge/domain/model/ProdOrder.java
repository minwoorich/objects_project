package com.objects.marketbridge.domain.model;

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
    @GeneratedValue
    @Column(name = "prod_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private String statusCode; // 00, DELIVERED, CANCEL, RETURN, EXCHANGE

    private Long totalPrice;

    private Integer pointRate; // 적립율

    private Integer savedPoint;

    private LocalDateTime deliveredDate;

    @OneToMany(mappedBy = "prodOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdOrderDetail> prodOrderDetails = new ArrayList<>();

    @Builder
    private ProdOrder(User user, Address address, String statusCode, Long totalPrice, Integer pointRate, Integer savedPoint, LocalDateTime deliveredDate) {
        this.user = user;
        this.address = address;
        this.statusCode = statusCode;
        this.totalPrice = totalPrice;
        this.pointRate = pointRate;
        this.savedPoint = savedPoint;
        this.deliveredDate = deliveredDate;
    }

    public void addOrderDetail(ProdOrderDetail prodOrderDetails) {
        this.prodOrderDetails.add(prodOrderDetails);
        prodOrderDetails.setOrder(this);
    }



//    public OrderCreate to() {
//        return OrderCreate.builder()
//                .
//    }
}
