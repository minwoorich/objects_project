package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageJpaRepository extends JpaRepository<ProductImage, Long> {
}
