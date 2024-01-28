package com.objects.marketbridge.domain.order.infra;

import com.objects.marketbridge.domain.order.domain.OrderDetail;
import com.objects.marketbridge.common.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailJpaRepository extends JpaRepository<OrderDetail, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update OrderDetail pod set pod.statusCode = :type where pod.order.id = :orderId")
    int changeAllType(@Param("orderId") Long orderId, @Param("type") String type);

    @Modifying(clearAutomatically = true)
    @Query("update OrderDetail pod set pod.reason = :reason where pod.order.id = :orderId")
    void addReason(@Param("orderId") Long orderId, @Param("reason") String reason);

    List<OrderDetail> findByProductId(Long memberId);

    List<OrderDetail> findByOrderNo(String orderNo);

    List<OrderDetail> findByOrder_IdAndProductIn(Long orderId, List<Product> products);

    List<OrderDetail> findByOrderNoAndProduct_IdIn(String orderNo, List<Long> productIds);

}
