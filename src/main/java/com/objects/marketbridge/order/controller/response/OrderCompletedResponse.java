package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.common.domain.AddressValue;
import com.objects.marketbridge.payment.domain.Amount;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@NotNull
public class OrderCompletedResponse {

    private AddressValue addressValue;
    private Amount amount;
    private List<ProductInfoResponse> productInfos;
    private Long deliveryFee;

    @Builder
    public OrderCompletedResponse(AddressValue addressValue, Amount amount, List<ProductInfoResponse> productInfos, Long deliveryFee) {
        this.addressValue = addressValue;
        this.amount = amount;
        this.productInfos = productInfos;
        this.deliveryFee = deliveryFee;
    }
}
