package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.model.ProdOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<ProdOrder, Long> {
    Optional<ProdOrder> findById(Long id);
    ProdOrder getById(Long id);

    Optional<ProdOrder> findByOrderNo();
}
