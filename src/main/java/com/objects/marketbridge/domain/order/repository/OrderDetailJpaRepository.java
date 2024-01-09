package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.model.ProdOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailJpaRepository extends JpaRepository<ProdOrderDetail, Long> {

}
