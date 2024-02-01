package com.objects.marketbridge.seller.service.port;

import com.objects.marketbridge.seller.domain.SellerAccount;

public interface SellerAccountRepository {

    SellerAccount findById(Long id);

    SellerAccount findBySellerId(Long sellerId);

    SellerAccount save(SellerAccount sellerAccount);
}
