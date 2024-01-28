package com.objects.marketbridge.domain.seller.infra;

import com.objects.marketbridge.domain.seller.domain.SellerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SellerAccountJpaRepository extends JpaRepository<SellerAccount, Long> {

    @Query("select sc from SellerAccount sc where sc.seller.id = :sellerId ")
    SellerAccount findBySellerId(Long sellerId);
}
