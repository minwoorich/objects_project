package com.objects.marketbridge.domain.order.controller.request;

import com.objects.marketbridge.global.error.DataVerificationFailedException;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CheckoutRequest {

    @NotNull
    private String orderId;

    @NotNull
    private Long amount;

    @NotNull
    private List<String> products;

    @NotNull
    private List<String> coupons;

    @Builder
    public CheckoutRequest(String orderId, Long amount, List<String> products, List<String> coupons) {
        valid(products, coupons);
        this.orderId = orderId;
        this.amount = amount;
        this.products = products;
        this.coupons = coupons;
    }

    private void valid(List<String> products, List<String> coupons) {
        if (products.size() != coupons.size()) {
            throw new DataVerificationFailedException("products 와 coupons 의 길이가 다릅니다. " +
                    "products = "+products.size() +", coupons = "+coupons.size());
        }
    }
}
