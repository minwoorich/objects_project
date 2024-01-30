package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.common.domain.Address;
import com.objects.marketbridge.common.domain.BaseEntity;
import com.objects.marketbridge.common.domain.Member;
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
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private String orderName;

    private String orderNo;

    private Long totalDiscount; // 총 할인 금액 (쿠폰,포인트,멤버쉽)

    private Long totalPrice; // 총 금액

    private Long realPrice; // 실 결제 금액

    private String tid;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderDetail> orderDetails = new ArrayList<>();

    @Builder
    public Order(Member member, Address address, String orderName, String orderNo, Long realPrice, Long totalPrice, Long totalDiscount, String tid) {
        this.member = member;
        this.address = address;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.realPrice = realPrice;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.tid = tid;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    //== 비즈니스 로직==//
    public void cancelReturn(String reason, String statusCode, LocalDateTime cancelDateTime) {
        changeUpdateAt(cancelDateTime);
        orderDetails.forEach(orderDetail -> orderDetail.cancel(reason, statusCode));
    }

    public void returnCoupon() {

        orderDetails.stream()
                .filter(o -> o.getCoupon() != null)
                .forEach(OrderDetail::returnCoupon);
    }

    public void useCoupon(LocalDateTime dateTime) {

        orderDetails.stream()
                .filter(o -> o.getCoupon() != null)
                .forEach(o -> o.useCoupon(dateTime));
    }

    public void changeStatusCode(String statusCode) {
        orderDetails.forEach(orderDetail -> orderDetail.changeStatusCode(statusCode));
    }

    public static Order create(Member member, Address address, String orderName, String orderNo, Long totalPrice, String tid){

        return Order.builder()
                .member(member)
                .address(address)
                .orderName(orderName)
                .orderNo(orderNo)
                .totalPrice(totalPrice)
                .tid(tid)
                .build();
    }
    public void calcTotalDiscount(CalcTotalDiscountService service) {

        totalDiscount = service.calculate(this);
        realPrice = totalPrice - totalDiscount;
    }

    public void stockDecrease() {
        orderDetails.forEach(o -> o.getProduct().decrease(o.getQuantity()));
    }
}
