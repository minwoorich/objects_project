package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.dto.RefundDto;
import com.objects.marketbridge.domain.payment.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.objects.marketbridge.domain.order.domain.StatusCodeType.*;

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
//        // TODO findStockByProdOrderId TEST 진행
//        List<Stock> stocks = stockRepository.findStocksByProdOrderId(orderId);
//
//        for (Stock stock : stocks) {
//            // TODO findByStockIdAndOrderId TEST 진행
//            ProdOrderDetail prodOrderDetail = orderDetailRepository.findByStockIdAndOrderId(stock.getId(), orderId);
//            if (prodOrderDetail != null) {
//                prodOrderDetail.cancel(reason, ORDER_CANCEL.getCode());
//                stock.increase(prodOrderDetail.getQuantity());
//            }
//        }
    }

}
