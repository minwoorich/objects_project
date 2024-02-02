package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.controller.response.OrderCancelReturnResponse;
import com.objects.marketbridge.order.controller.response.ProductResponse;
import com.objects.marketbridge.order.controller.response.RefundInfo;
import com.objects.marketbridge.order.service.dto.CancelReturnResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ConfirmCancelReturnHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long orderId;
        private String orderNumber;
        private Long totalPrice;
        private LocalDateTime cancellationDate; // 주문 취소 일자
        private List<ProductResponse> cancelledItems;
        private RefundInfo refundInfo;

        @Builder
        private Response(Long orderId, String orderNumber, Long totalPrice, LocalDateTime cancellationDate, RefundInfo refundInfo, List<ProductResponse> cancelledItems) {
            this.orderId = orderId;
            this.orderNumber = orderNumber;
            this.totalPrice = totalPrice;
            this.cancellationDate = cancellationDate;
            this.refundInfo = refundInfo;
            this.cancelledItems = cancelledItems;
        }

        public static Response of(CancelReturnResponseDto serviceDto) {
            return Response.builder()
                    .orderId(serviceDto.getOrderId())
                    .orderNumber(serviceDto.getOrderNumber())
                    .totalPrice(serviceDto.getTotalPrice())
                    .cancellationDate(serviceDto.getCancellationDate())
                    .refundInfo(serviceDto.getRefundInfo())
                    .cancelledItems(serviceDto.getCancelledItems())
                    .build();

        }
    }
}
