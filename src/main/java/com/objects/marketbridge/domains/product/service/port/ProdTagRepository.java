package com.objects.marketbridge.domains.product.service.port;

import com.objects.marketbridge.domains.product.domain.ProdTag;

import java.util.List;

public interface ProdTagRepository {
    void saveAll(List<ProdTag> prodTags);
}
