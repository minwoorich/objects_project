package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.ProdTag;

import java.util.List;

public interface ProdTagRepository {
    void saveAll(List<ProdTag> prodTags);
}
