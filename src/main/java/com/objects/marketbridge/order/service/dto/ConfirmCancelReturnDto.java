package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.controller.response.ProductResponse;
import com.objects.marketbridge.order.controller.response.RefundInfo;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ConfirmCancelReturnDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private String orderNo;
        private String cancelReason;

        @Builder
        private Request(String orderNo, String cancelReason) {
            this.orderNo = orderNo;
            this.cancelReason = cancelReason;
        }
    }

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

        public static Response of(Order order, RefundDto refundDto, DateTimeHolder dateTimeHolder) {
            return Response.builder()
                    .orderId(order.getId())
                    .orderNumber(order.getOrderNo())
                    .totalPrice(order.getOrderDetails().stream()
                            .mapToLong(OrderDetail::totalAmount)
                            .sum()
                    )
                    .cancellationDate(dateTimeHolder.getUpdateTime(order))
                    .refundInfo(RefundInfo.of(refundDto))
                    .cancelledItems(
                            order.getOrderDetails().stream()
                                    .map(orderDetail -> ProductResponse.of(orderDetail.getProduct(), orderDetail.getQuantity()))
                                    .toList()
                    )
                    .build();
        }
    }

}
