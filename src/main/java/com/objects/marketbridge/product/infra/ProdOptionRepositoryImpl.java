package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.product.domain.ProdOption;
import com.objects.marketbridge.product.service.port.ProdOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProdOptionRepositoryImpl implements ProdOptionRepository {

    private final ProdOptionJpaRepository prodOptionJpaRepository;

    @Override
    public void save(ProdOption prodOption) {
        prodOptionJpaRepository.save(prodOption);
    }
}
