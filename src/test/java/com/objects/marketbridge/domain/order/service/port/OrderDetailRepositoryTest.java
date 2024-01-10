package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.model.ProdOrder;
import com.objects.marketbridge.domain.model.ProdOrderDetail;
import com.objects.marketbridge.domain.model.StatusCodeType;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class OrderDetailRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("특정 주문에 해당하는 주문 상세의 상태 코드를 한번에 바꾼다.")
    public void changeAllType() {
        // given
        String givenType = StatusCodeType.PAYMENT_COMPLETED.getCode();
        String changeType = StatusCodeType.ORDER_CANCEL.getCode();

        ProdOrderDetail orderDetail1 = createOrderDetail(givenType);
        ProdOrderDetail orderDetail2 = createOrderDetail(givenType);
        ProdOrderDetail orderDetail3 = createOrderDetail(givenType);

        ProdOrder order = ProdOrder.builder().build();
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        order.addOrderDetail(orderDetail3);

        ProdOrder savedOrder = orderRepository.save(order);

        // when
        int result = orderDetailRepository.changeAllType(savedOrder.getId(), changeType);

        // then
        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("주문 상세 리스트를 모두 저장한다.")
    public void saveAll() {
        // given
        String givenType = StatusCodeType.PAYMENT_COMPLETED.getCode();
        ProdOrderDetail orderDetail1 = createOrderDetail(givenType);
        ProdOrderDetail orderDetail2 = createOrderDetail(givenType);
        ProdOrderDetail orderDetail3 = createOrderDetail(givenType);
        List<ProdOrderDetail> orderDetails = List.of(orderDetail1, orderDetail2, orderDetail3);

        // when
        List<ProdOrderDetail> savedOrderDetails = orderDetailRepository.saveAll(orderDetails);

        // then
        assertThat(savedOrderDetails.size()).isEqualTo(3);
    }

    private ProdOrderDetail createOrderDetail(String code) {
        return ProdOrderDetail.builder()
                .statusCode(code)
                .build();
    }
}