package com.objects.marketbridge.order.controller.request;

import com.objects.marketbridge.order.service.dto.CreateOrderDto;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.order.domain.ProductValue;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderRequest {

//    @NotNull
    private Long amount;

//    @NotNull
    private Long addressId;

//    @NotNull
    private String orderName;

//    @NotNull
    private List<ProductValue> productValues;

    @Builder
    private CreateOrderRequest(Long amount, Long addressId, String orderName, @Singular List<ProductValue> productValues) {
        this.amount = amount;
        this.addressId = addressId;
        this.orderName = orderName;
        this.productValues = productValues;
    }

    public CreateOrderDto toDto(Long memberId, String tid) {
        return CreateOrderDto.builder()
                .memberId(memberId)
                .tid(tid)
                .addressId(addressId)
                .orderName(orderName)
                .totalOrderPrice(amount)
                .productValues(productValues)
                .build();
    }

    public static CreateOrderRequest of(CreateOrderDto dto) {
        return CreateOrderRequest.builder()
                .addressId(dto.getAddressId())
                .orderName(dto.getOrderName())
                .amount(dto.getTotalOrderPrice())
                .productValues(dto.getProductValues())
                .build();
    }

    public KakaoPayReadyRequest toKakaoReadyRequest(String orderNo, Long memberId, String cid, String approvalUrl, String failUrl, String cancelUrl) {
        return KakaoPayReadyRequest.builder()
                .cid(cid)
                .approvalUrl(approvalUrl)
                .failUrl(failUrl)
                .cancelUrl(cancelUrl)
                .quantity(productValues.stream().mapToLong(ProductValue::getQuantity).sum())
                .partnerOrderId(orderNo)
                .partnerUserId(memberId.toString())
                .itemName(orderName)
                .taxFreeAmount(0L)
                .totalAmount(amount)
                .build();
    }
}
