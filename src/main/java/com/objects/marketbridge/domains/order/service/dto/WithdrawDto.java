package com.objects.marketbridge.domains.order.service.dto;

import com.objects.marketbridge.domains.order.domain.OrderCancelReturn;
import com.objects.marketbridge.domains.payment.service.dto.PaymentDto;

public class WithdrawDto {



    public static class Response {

        public static Response of(OrderCancelReturn orderReturn, PaymentDto paymentDto) {
            return null;
        }
    }
}
