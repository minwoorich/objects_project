package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.OrderCancelReturn;
import com.objects.marketbridge.payment.service.dto.PaymentDto;

public class WithdrawDto {



    public static class Response {

        public static Response of(OrderCancelReturn orderReturn, PaymentDto paymentDto) {
            return null;
        }
    }
}
