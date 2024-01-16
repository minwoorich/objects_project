package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.entity.ProdOrderDetail;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.dto.RefundDto;
import com.objects.marketbridge.domain.payment.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.objects.marketbridge.domain.order.entity.StatusCodeType.*;

@Service
@RequiredArgsConstructor
public class OrderCancelService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final RefundService refundService;

    public void orderCancel(Long orderId, String reason) {
        InnerService innerService = new InnerService();

        innerService.cancel(orderId, reason);

        // TODO 결제 취소(환불)
        refundService.refund("계좌번호", 10000L);
    }


    // TODO 객체로 따로 빼야함(임시로 사용)
    class InnerService {
        @Transactional
        public void cancel(Long orderId, String reason) {
            ProdOrder prodOrder = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 없습니다."));

            prodOrder.cancel(reason, ORDER_CANCEL.getCode());

            prodOrder.returnCoupon();

            // TODO 포인트 반환

        }

    }

}
