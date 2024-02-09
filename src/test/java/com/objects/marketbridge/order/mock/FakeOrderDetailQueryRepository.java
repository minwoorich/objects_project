package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.ORDERDETAIL_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class FakeOrderDetailQueryRepository extends BaseFakeOrderDetailRepository implements OrderDetailQueryRepository {
    @Override
    public OrderDetail findById(Long id) {
        return getInstance().getData().stream()
                .filter(od -> od.getId().equals(id))
                .findAny()
                .orElseThrow(
                        () -> new EntityNotFoundException("엔티티가 존재하지 않습니다")
                );
    }

    @Override
    public List<OrderDetail> findByProductId(Long id) {
        return null;
    }

    @Override
    public List<OrderDetail> findAll() {
        return null;
    }

    @Override
    public List<OrderDetail> findByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public List<OrderDetail> findByOrder_IdAndProductIn(Long orderId, List<Product> products) {
        return null;
    }

    @Override
    public List<OrderDetail> findByOrderNoAndProduct_IdIn(String orderNo, List<Long> productIds) {
        return getInstance().getData().stream()
                .filter(od -> od.getOrder().getOrderNo().equals(orderNo)
                        && productIds.contains(od.getProduct().getId()))
                .toList();
    }

    @Override
    public List<OrderDetail> findByIdIn(List<Long> orderDetailIds) {
        return getInstance().getData().stream()
                .filter(orderDetail -> orderDetailIds.contains(orderDetail.getId()))
                .toList();
    }

    @Override
    public List<OrderDetail> findByOrderNoAndOrderDetail_In(String orderNo, List<Long> orderDetailIds) {
        return getInstance().getData().stream()
                .filter(od -> od.getOrder().getOrderNo().equals(orderNo)
                        && orderDetailIds.contains(od.getId()))
                .toList();
    }
}
