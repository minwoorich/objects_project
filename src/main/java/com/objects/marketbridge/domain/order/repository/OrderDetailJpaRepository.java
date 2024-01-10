package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.model.ProdOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailJpaRepository extends JpaRepository<ProdOrderDetail, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update ProdOrderDetail pod set pod.statusCode = :type where pod.prodOrder.id = :orderId")
    int changeAllType(@Param("orderId") Long orderId, @Param("type") String type);

}
