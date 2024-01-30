package com.objects.marketbridge.seller.infra;

import com.objects.marketbridge.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SellerJpaRepository extends JpaRepository<Seller, Long> {

}
