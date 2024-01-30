package com.objects.marketbridge.seller.service.port;

import com.objects.marketbridge.common.domain.Option;
import com.objects.marketbridge.seller.domain.Seller;
import com.objects.marketbridge.seller.domain.SellerAccount;

import java.util.List;
import java.util.Optional;

public interface SellerRepository {

    Seller findById(Long id);

    Seller findWithSellerAccountById(Long id);
    List<Seller> findAllSeller();
    Seller save(Seller seller);
}
