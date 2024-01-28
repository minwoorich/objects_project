package com.objects.marketbridge.seller.service.port;

import com.objects.marketbridge.seller.domain.Seller;
import com.objects.marketbridge.seller.domain.SellerAccount;

import java.util.List;

public interface SellerRepository {

    Seller findSellerById(Long id);
    List<SellerAccount> findAllSellerAccount();
    List<Seller> findAllSeller();

    SellerAccount findSellerAccountBySellerId(Long sellerId);

    SellerAccount save(SellerAccount sellerAccount);
    Seller save(Seller seller);
}
