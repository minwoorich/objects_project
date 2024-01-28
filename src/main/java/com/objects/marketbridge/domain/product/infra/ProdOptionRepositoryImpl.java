package com.objects.marketbridge.domain.product.infra;

import com.objects.marketbridge.common.domain.ProdOption;
import com.objects.marketbridge.domain.product.service.port.ProdOptionRepository;
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
