package com.objects.marketbridge.order.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Getter
@NoArgsConstructor
public class PaymentRequest {
    String paymentKey; // 결제 키값
    String orderId; // 주문ID
    String amount; // 결제할 금액

}
