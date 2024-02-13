package com.objects.marketbridge.cart.service.port;

import com.objects.marketbridge.cart.domain.Cart;

public interface CartCommendRepository {

    Cart save(Cart cart);

}
