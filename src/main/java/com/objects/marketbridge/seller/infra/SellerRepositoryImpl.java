package com.objects.marketbridge.seller.infra;

import com.objects.marketbridge.seller.domain.Seller;
import com.objects.marketbridge.seller.domain.SellerAccount;
import com.objects.marketbridge.seller.service.port.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SellerRepositoryImpl implements SellerRepository {

    private final SellerJpaRepository sellerJpaRepository;
    private final SellerAccountJpaRepository sellerAccountJpaRepository;

    @Override
    public Seller findSellerById(Long id) {
        return sellerJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<SellerAccount> findAllSellerAccount() {
        return sellerAccountJpaRepository.findAll();
    }

    @Override
    public List<Seller> findAllSeller() {
        return sellerJpaRepository.findAll();
    }

    @Override
    public SellerAccount findSellerAccountBySellerId(Long sellerId) {
        return sellerAccountJpaRepository.findBySellerId(sellerId);
    }

    @Override
    public SellerAccount save(SellerAccount sellerAccount) {
        return sellerAccountJpaRepository.save(sellerAccount);
    }

    @Override
    public Seller save(Seller seller) {
        return sellerJpaRepository.save(seller);
    }
}
