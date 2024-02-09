package com.objects.marketbridge.order.controller.dto.select;

import com.objects.marketbridge.member.domain.AddressValue;
import lombok.Builder;

public class GetOrderDetailHttp {
    public static class Response{
        private OrderInfo orderInfo;
        private AddressValue addressValue;
        private PaymentInfo paymentInfo;

        @Builder
        public Response(OrderInfo orderInfo, AddressValue addressValue, PaymentInfo paymentInfo) {
            this.orderInfo = orderInfo;
            this.addressValue = addressValue;
            this.paymentInfo = paymentInfo;
        }

        public static Response create(OrderInfo orderInfo, AddressValue addressValue, PaymentInfo paymentInfo) {
            return Response.builder()
                    .orderInfo(orderInfo)
                    .addressValue(addressValue)
                    .paymentInfo(paymentInfo)
                    .build();
        }
    }

}
