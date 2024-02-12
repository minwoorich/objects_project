package com.objects.marketbridge.payment.controller.dto;

import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.dto.ProductInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
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
        private String approvedAt;
        private Long totalAmount;
        private Long discountAmount;
        private Long taxFreeAmount;
        private String cardIssuerName;
        private String cardPurchaseName;
        private String cardInstallMonth;
        private AddressValue addressValue;
        private List<ProductInfoDto> productInfos;

        @Builder
        private Response(String paymentMethodType, String orderName, String approvedAt, Long totalAmount, Long discountAmount, Long taxFreeAmount, String cardIssuerName, String cardPurchaseName, String cardInstallMonth, AddressValue addressValue, List<ProductInfoDto> productInfos) {
            this.paymentMethodType = paymentMethodType;
            this.orderName = orderName;
            this.approvedAt = approvedAt;
            this.totalAmount = totalAmount;
            this.discountAmount = discountAmount;
            this.taxFreeAmount = taxFreeAmount;
            this.cardIssuerName = cardIssuerName;
            this.cardPurchaseName = cardPurchaseName;
            this.cardInstallMonth = cardInstallMonth;
            this.addressValue = addressValue;
            this.productInfos = productInfos;
        }

        public static Response of(Payment payment) {
            return Response.builder()
                    .paymentMethodType(payment.getPaymentMethod())
                    .orderName(payment.getOrder().getOrderName())
                    .approvedAt(payment.getApprovedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .cardIssuerName(payment.getCardInfo().getCardIssuerName())
                    .cardPurchaseName(payment.getCardInfo().getCardPurchaseName())
                    .cardInstallMonth(payment.getCardInfo().getCardInstallMonth())
                    .totalAmount(payment.getAmount().getTotalAmount())
                    .discountAmount(payment.getAmount().getDiscountAmount())
                    .taxFreeAmount(payment.getAmount().getTaxFreeAmount())
                    .addressValue(payment.getOrder().getAddress().getAddressValue())
                    .productInfos(createProductInfoDtos(payment.getOrder().getOrderDetails()))
                    .build();
        }

        public static Response create(String paymentMethodType, String orderName, String approvedAt, Long totalAmount, Long discountAmount, Long taxFreeAmount, String cardIssuerName, String cardPurchaseName, String cardInstallMonth, AddressValue addressValue, List<ProductInfoDto> productInfos) {
            return Response.builder()
                    .paymentMethodType(paymentMethodType)
                    .orderName(orderName)
                    .approvedAt(approvedAt)
                    .totalAmount(totalAmount)
                    .discountAmount(discountAmount)
                    .taxFreeAmount(taxFreeAmount)
                    .cardIssuerName(cardIssuerName)
                    .cardPurchaseName(cardPurchaseName)
                    .cardInstallMonth(cardInstallMonth)
                    .addressValue(addressValue)
                    .productInfos(productInfos)
                    .build();
        }

        // TODO : productInfos -> sellerName,deliveredDate 미완성
        private static List<ProductInfoDto> createProductInfoDtos(List<OrderDetail> orderDetails) {
            return orderDetails.stream().map(o -> ProductInfoDto.of(o.getProduct())).collect(Collectors.toList());
        }
    }

}
