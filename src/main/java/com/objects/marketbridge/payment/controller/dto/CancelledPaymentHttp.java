package com.objects.marketbridge.payment.controller.dto;

import com.objects.marketbridge.common.dto.KakaoPayOrderResponse;
import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.dto.ProductInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CancelledPaymentHttp {

    @Getter
    @NoArgsConstructor
    public static class Response{

        private String paymentMethodType;
        private String orderName;
        private String approvedAt;
        private String canceledAt;
        private String status;
        private Long totalAmount;
        private Long discountAmount;
        private Long taxFreeAmount;
        private String cardIssuerName;
        private String cardPurchaseName;
        private String cardInstallMonth;
        private List<ProductInfoDto> productInfos;

        @Builder
        private Response(String paymentMethodType, String orderName, String approvedAt, String canceledAt, String status, Long totalAmount, Long discountAmount, Long taxFreeAmount, String cardIssuerName, String cardPurchaseName, String cardInstallMonth, List<ProductInfoDto> productInfos) {
            this.paymentMethodType = paymentMethodType;
            this.orderName = orderName;
            this.approvedAt = approvedAt;
            this.canceledAt = canceledAt;
            this.status = status;
            this.totalAmount = totalAmount;
            this.discountAmount = discountAmount;
            this.taxFreeAmount = taxFreeAmount;
            this.cardIssuerName = cardIssuerName;
            this.cardPurchaseName = cardPurchaseName;
            this.cardInstallMonth = cardInstallMonth;
            this.productInfos = productInfos;
        }

        public static CancelledPaymentHttp.Response of(KakaoPayOrderResponse kakaoResp, Order order) {
            return CancelledPaymentHttp.Response.builder()
                    .paymentMethodType(kakaoResp.getPaymentMethodType())
                    .orderName(kakaoResp.getItemName())
                    .approvedAt(kakaoResp.getApprovedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .cardIssuerName(kakaoResp.getSelectedCardInfo().getCardIssuerName())
                    .cardPurchaseName(kakaoResp.getSelectedCardInfo().getCardPurchaseName())
                    .cardInstallMonth(kakaoResp.getSelectedCardInfo().getCardInstallMonth())
                    .totalAmount(kakaoResp.getAmount().getTotalAmount())
                    .discountAmount(kakaoResp.getAmount().getDiscountAmount())
                    .taxFreeAmount(kakaoResp.getAmount().getTaxFreeAmount())
                    .productInfos(createProductInfoDtos(order.getOrderDetails()))
                    .build();
        }

        public static Response create(String paymentMethodType, String orderName, String approvedAt, String canceledAt, String status, Long totalAmount, Long discountAmount, Long taxFreeAmount, String cardIssuerName, String cardPurchaseName, String cardInstallMonth,  List<ProductInfoDto> productInfos) {
            return Response.builder()
                    .paymentMethodType(paymentMethodType)
                    .orderName(orderName)
                    .approvedAt(approvedAt)
                    .canceledAt(canceledAt)
                    .status(status)
                    .totalAmount(totalAmount)
                    .discountAmount(discountAmount)
                    .taxFreeAmount(taxFreeAmount)
                    .cardIssuerName(cardIssuerName)
                    .cardPurchaseName(cardPurchaseName)
                    .cardInstallMonth(cardInstallMonth)
                    .productInfos(productInfos)
                    .build();
        }

        // TODO : productInfos -> sellerName,deliveredDate 미완성
        private static List<ProductInfoDto> createProductInfoDtos(List<OrderDetail> orderDetails) {
            return orderDetails.stream().map(o -> ProductInfoDto.of(o.getProduct())).collect(Collectors.toList());
        }
    }
}
