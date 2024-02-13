package com.objects.marketbridge.cart.infra;

import com.objects.marketbridge.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.product.productNo = :productNo")
    Optional<Cart> findByProductNo(@Param("productNo") String productNo);
}
