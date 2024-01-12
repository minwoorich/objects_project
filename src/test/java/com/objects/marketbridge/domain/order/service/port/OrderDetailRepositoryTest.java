package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.order.domain.ProdOrder;
import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
import com.objects.marketbridge.domain.order.domain.StatusCodeType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OrderDetailRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    @Test
    @DisplayName("특정 주문에 해당하는 주문 상세의 상태 코드를 한번에 바꾼다.")
    public void changeAllType() {
        // given
        String givenCodeType = StatusCodeType.PAYMENT_COMPLETED.getCode();
        String changeCodeType = StatusCodeType.ORDER_CANCEL.getCode();

        ProdOrderDetail orderDetail1 = createOrderDetail_type(givenCodeType);
        ProdOrderDetail orderDetail2 = createOrderDetail_type(givenCodeType);
        ProdOrderDetail orderDetail3 = createOrderDetail_type(givenCodeType);

        ProdOrder order = ProdOrder.builder().build();
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        order.addOrderDetail(orderDetail3);

        ProdOrder savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();

        // when
        int result = orderDetailRepository.changeAllType(orderId, changeCodeType);

        // then
        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("주문 상세 리스트를 모두 저장한다.")
    public void saveAll() {
        // given
        String givenType = StatusCodeType.PAYMENT_COMPLETED.getCode();
        ProdOrderDetail orderDetail1 = createOrderDetail_type(givenType);
        ProdOrderDetail orderDetail2 = createOrderDetail_type(givenType);
        ProdOrderDetail orderDetail3 = createOrderDetail_type(givenType);
        List<ProdOrderDetail> orderDetails = List.of(orderDetail1, orderDetail2, orderDetail3);

        // when
        List<ProdOrderDetail> savedOrderDetails = orderDetailRepository.saveAll(orderDetails);

        // then
        assertThat(savedOrderDetails.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("주문 상세에 이유 넣기")
    public void addReason() {
        // given
        String reason = "상품이 맘에들지 않아요";
        ProdOrderDetail orderDetail1 = ProdOrderDetail.builder().build();
        ProdOrderDetail orderDetail2 = ProdOrderDetail.builder().build();
        ProdOrderDetail orderDetail3 = ProdOrderDetail.builder().build();

        ProdOrder order = ProdOrder.builder().build();
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        order.addOrderDetail(orderDetail3);

        ProdOrder savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();

        // when
        orderDetailRepository.addReason(orderId, reason);

        // then
        String savedReason = getReason(orderId);
        assertThat(reason).isEqualTo(savedReason);
    }

    private String getReason(Long orderId) {
        List<ProdOrderDetail> prodOrderDetails = orderRepository.findById(orderId).get().getProdOrderDetails();
        return prodOrderDetails.get(0).getReason();
    }

    private ProdOrderDetail createOrderDetail_type(String code) {
        return ProdOrderDetail.builder()
                .statusCode(code)
                .build();
    }


}