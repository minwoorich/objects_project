package com.objects.marketbridge.cart.service.port;

import com.objects.marketbridge.cart.domain.Cart;

import java.util.Optional;

public interface CartQueryRepository {
    Optional<Cart> findByProductNo(String productNo);

    Cart findById(Long id);


}
