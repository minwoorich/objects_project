package com.objects.marketbridge.domain.seller.infra;

import com.objects.marketbridge.domain.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerJpaRepository extends JpaRepository<Seller, Long> {
}
