package com.objects.marketbridge.domain.order.controller.request;

import com.objects.marketbridge.domain.order.dto.ProductInfoDto;
import com.objects.marketbridge.domain.order.exception.exception.CustomLogicException;
import com.objects.marketbridge.domain.order.exception.exception.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateOrderRequest {

    @NotNull
    private String payMethod;

    @NotNull
    private String orderName;

    @NotNull
    private Long totalOrderPrice;

    private String successUrl;

    private String failUrl;

    private List<ProductInfoDto> productInfos = new ArrayList<>();

    @Builder(builderClassName = "requestBuilder", toBuilder = true)
    public CreateOrderRequest(String payMethod, String orderName, Long totalOrderPrice, String successUrl, String failUrl, @Singular("product") List<ProductInfoDto> productInfos) {
        validPaymentAmount();
        this.payMethod = payMethod;
        this.orderName = orderName;
        this.totalOrderPrice = totalOrderPrice;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
        this.productInfos = productInfos;
    }

    private void validPaymentAmount() {
        if (totalOrderPrice > 1000000000 || totalOrderPrice < 1000) {
            throw new CustomLogicException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }
}
