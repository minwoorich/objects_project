package com.objects.marketbridge.cart.service;

import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.dto.UpdateCartDto;
import com.objects.marketbridge.cart.service.port.CartQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateCartService {

    private final CartQueryRepository cartQueryRepository;
    public void update(UpdateCartDto dto) {
        Cart cart = cartQueryRepository.findById(dto.getCartId());
        cart.updateQuantity(dto.getQuantity());
    }
}
