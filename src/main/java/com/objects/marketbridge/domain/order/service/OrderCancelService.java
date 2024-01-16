package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.domain.ProdOrder;
import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
import com.objects.marketbridge.domain.order.domain.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.dto.RefundDto;
import com.objects.marketbridge.domain.payment.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderCancelService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final RefundService refundService;

    public void orderCancel(Long orderId, String reason) {
        cancel(orderId, reason);

        // TODO 환불
        // ProdOrder에 존재하는 totalPrice의 값을 주문 유저의 계좌로 환불해주자.
        // (재고 수량 동시성 예외가 터져도 환불은 해줌 -> 재고 문제는 따로 처리해야 함)
        RefundDto refundDto =  refundService.refund("계좌번호", 10000f);
    }

    @Transactional
    // TODO 객체로 따로 빼야함
    public void cancel(Long orderId, String reason) {
        // orderId로 주문을 조회한다.
        ProdOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("조회된 주문이 없습니다."));

        // TODO 주문 취소하기 (주문 타입 변경 + reason 넣기)
        List<Product> products = order.cancel(reason);


//        orderDetailRepository.changeAllType(orderId, StatusCodeType.ORDER_CANCEL.getCode());
//        orderDetailRepository.addReason(orderId, reason);

        // TODO 재고의 수량을 늘리기 (동시성 문제 고려)
        // 고려사항 : 쿠팡이라면 orderDetail에 해당하는 상품들이 어디 wherehouse의 상품인지 알고있어야 한다.
        // 1. orderDetails에 해당하는 상품들을 리스트로 만들자.
        // 2. 상품 리스트들을 조건으로 상품 stock을 가지고 오자.
        // 3. stock의 재고량을 orderDetaeil의 quantity만큼 증가시키자.
    }

}
