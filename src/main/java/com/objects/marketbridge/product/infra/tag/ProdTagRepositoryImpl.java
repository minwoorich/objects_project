package com.objects.marketbridge.product.infra.tag;

import com.objects.marketbridge.product.domain.ProdTag;
import com.objects.marketbridge.product.service.port.ProdTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProdTagRepositoryImpl implements ProdTagRepository {
    private final ProdTagJpaRepository prodTagJpaRepository;

    @Override
    public void saveAll(List<ProdTag> prodTags) {
        prodTagJpaRepository.saveAll(prodTags);
    }
}
