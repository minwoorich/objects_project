package com.objects.marketbridge.domain.order.controller.request;

import com.objects.marketbridge.global.error.DataVerificationException;
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

    @Builder
    public CheckoutRequest(String orderId, Long amount, List<String> products) {
        valid(products);
        this.orderId = orderId;
        this.amount = amount;
        this.products = products;
    }

    private void valid(List<String> products) {
        int size = products.size();
        if (size == 0 || size % 2 == 1) {
            throw new DataVerificationException("유효성검사 실패");
        }
    }
}
