package com.objects.marketbridge.cart.infra;

import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.port.CartQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CartQueryRepositoryImpl implements CartQueryRepository {

    private final CartJpaRepository cartJpaRepository;

    @Override
    public Optional<Cart> findByProductNo(String productNo) {
        return cartJpaRepository.findByProductNo(productNo);
    }
}
