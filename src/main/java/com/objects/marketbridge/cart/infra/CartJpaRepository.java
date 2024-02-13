package com.objects.marketbridge.cart.infra;

import com.objects.marketbridge.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
}
