package com.objects.marketbridge.payment.controller.dto;

import com.objects.marketbridge.common.domain.AddressValue;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.dto.ProductInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CompleteOrderHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {

        private String paymentMethodType;
        private String orderName;
        private LocalDateTime approvedAt;
        private Amount amount;
        private CardInfo cardInfo;
        private AddressValue addressValue;
        private List<ProductInfoDto> productInfos;

        @Builder
        private Response(String paymentMethodType, String orderName, LocalDateTime approvedAt, Amount amount, CardInfo cardInfo, AddressValue addressValue, List<ProductInfoDto> productInfos) {
            this.paymentMethodType = paymentMethodType;
            this.orderName = orderName;
            this.approvedAt = approvedAt;
            this.amount = amount;
            this.cardInfo = cardInfo;
            this.addressValue = addressValue;
            this.productInfos = productInfos;
        }

        public static Response of(Payment payment) {
            return Response.builder()
                    .paymentMethodType(payment.getPaymentMethod())
                    .orderName(payment.getOrder().getOrderName())
                    .approvedAt(payment.getApprovedAt())
                    .amount(payment.getAmount())
                    .cardInfo(payment.getCardInfo())
                    .addressValue(payment.getOrder().getAddress().getAddressValue())
                    .productInfos(createProductInfoDtos(payment.getOrder().getOrderDetails()))
                    .build();
        }

        // TODO : productInfos -> sellerName,deliveredDate 미완성
        private static List<ProductInfoDto> createProductInfoDtos(List<OrderDetail> orderDetails) {
            return orderDetails.stream().map(o -> ProductInfoDto.of(o.getProduct())).collect(Collectors.toList());
        }
    }

}
