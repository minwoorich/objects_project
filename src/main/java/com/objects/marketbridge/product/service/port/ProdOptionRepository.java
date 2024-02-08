package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.ProdOption;

import java.util.List;

public interface ProdOptionRepository {

    void save(ProdOption prodOption);

    void saveAll(List<ProdOption> prodOptions);
}
