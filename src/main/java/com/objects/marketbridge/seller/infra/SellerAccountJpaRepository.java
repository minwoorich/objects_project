package com.objects.marketbridge.seller.infra;

import com.objects.marketbridge.seller.domain.SellerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SellerAccountJpaRepository extends JpaRepository<SellerAccount, Long> {

    @Query("select sc from SellerAccount sc where sc.seller.id = :sellerId ")
    SellerAccount findBySellerId(Long sellerId);
}
