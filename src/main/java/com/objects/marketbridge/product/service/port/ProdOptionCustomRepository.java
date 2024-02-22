package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.dto.OptionDto;

import java.util.List;

public interface ProdOptionCustomRepository {
    List<OptionDto> findAllByProductId(Long productId);
}
