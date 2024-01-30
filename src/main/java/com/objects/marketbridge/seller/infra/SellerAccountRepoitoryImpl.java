package com.objects.marketbridge.seller.infra;

import com.objects.marketbridge.seller.domain.SellerAccount;
import com.objects.marketbridge.seller.service.port.SellerAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellerAccountRepoitoryImpl implements SellerAccountRepository {

    private final SellerAccountJpaRepository sellerAccountJpaRepository;


    @Override
    public SellerAccount findById(Long id) {
        return sellerAccountJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public SellerAccount findBySellerId(Long sellerId) {
        return sellerAccountJpaRepository.findBySellerId(sellerId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public SellerAccount save(SellerAccount sellerAccount) {
        return sellerAccountJpaRepository.save(sellerAccount);
    }
}
