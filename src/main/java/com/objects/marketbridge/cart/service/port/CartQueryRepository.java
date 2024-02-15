package com.objects.marketbridge.cart.service.port;

import com.objects.marketbridge.cart.domain.Cart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface CartQueryRepository {
    Optional<Cart> findByProductNo(String productNo);

    Cart findById(Long id);

    Slice<Cart> findSlicedCart(Pageable pageable, Long memberId);


}
