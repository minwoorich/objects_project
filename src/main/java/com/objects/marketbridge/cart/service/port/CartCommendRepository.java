package com.objects.marketbridge.cart.service.port;

import com.objects.marketbridge.cart.domain.Cart;

import java.util.List;

public interface CartCommendRepository {

    Cart save(Cart cart);

    void saveAll(List<Cart> carts);

}
