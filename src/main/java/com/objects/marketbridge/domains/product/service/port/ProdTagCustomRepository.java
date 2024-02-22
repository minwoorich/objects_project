package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.dto.ProdTagDto;

import java.util.List;

public interface ProdTagCustomRepository {

    List<ProdTagDto> findAllByProductId(Long productId);
}
