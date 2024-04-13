package com.objects.marketbridge.domains.product.mock;

import com.objects.marketbridge.domains.product.domain.ProductGroup;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupCommandRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Objects;

public class FakeProductGroupCommandRepository extends BaseFakeProductGroupRepository implements ProductGroupCommandRepository {

    @Override
    public void save(ProductGroup productGroup) {
        if (productGroup.getId() == null || productGroup.getId() == 0) {
            ReflectionTestUtils.setField(productGroup, "id", increaseId(), Long.class);
            getInstance().getData().add(productGroup);
        } else {
            getInstance().getData().removeIf(item -> Objects.equals(item.getId(), productGroup.getId()));
            getInstance().getData().add(productGroup);
        }
    }

    @Override
    public void saveAll(List<ProductGroup> productGroups) {
        productGroups.forEach(this::save);
    }
}
