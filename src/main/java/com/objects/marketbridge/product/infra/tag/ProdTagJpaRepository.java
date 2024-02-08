package com.objects.marketbridge.product.infra.tag;

import com.objects.marketbridge.product.domain.ProdTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdTagJpaRepository extends JpaRepository<ProdTag,Long> {
}
