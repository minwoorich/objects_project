package com.objects.marketbridge.domains.product.mock;

import com.objects.marketbridge.domains.product.domain.ProductGroup;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.util.List;
import java.util.stream.Collectors;

public class FakeProductGroupQueryRepository extends BaseFakeProductGroupRepository implements ProductGroupQueryRepository {

    @Override
    public ProductGroup findById(Long id) {
        return BaseFakeProductGroupRepository.getInstance().getData().stream()
                .filter(p -> p.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException("엔티티가 존재하지 않습니다. [입력 id = "+id+"]")));
    }

    @Override
    public List<ProductGroup> findAllById(List<Long> ids) {
        return BaseFakeProductGroupRepository.getInstance().getData().stream()
                .filter(p -> ids.contains(p.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductGroup> findAll() {
        return BaseFakeProductGroupRepository.getInstance().getData();
    }
}
