package com.objects.marketbridge.domain.order.dto;

import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.payment.domain.Card;
import com.objects.marketbridge.domain.payment.domain.PaymentCancel;
import com.objects.marketbridge.domain.payment.domain.Transfer;
import com.objects.marketbridge.domain.payment.domain.VirtualAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderDto {
    private Long memberId;
    private Long addressId;
    private String orderName;
    private String orderNo;
    private Long totalOrderPrice;
    private Long realOrderPrice;
    private String paymentKey;
    private String paymentStatus;
    private String refundStatus;
    private String bankCode;
    private PaymentCancel paymentCancel;
    private List<ProductValue> productValues;



    @Builder
    public CreateOrderDto(Long memberId, Long addressId, String orderName, String orderNo, Long totalOrderPrice, Long realOrderPrice,  String paymentKey, String paymentStatus, String refundStatus, String bankCode, PaymentCancel paymentCancel, List<ProductValue> productValues) {
        this.memberId = memberId;
        this.addressId = addressId;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.totalOrderPrice = totalOrderPrice;
        this.realOrderPrice = realOrderPrice;
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.refundStatus = refundStatus;
        this.bankCode = bankCode;
        this.paymentCancel = paymentCancel;
        this.productValues = productValues;
    }
}
