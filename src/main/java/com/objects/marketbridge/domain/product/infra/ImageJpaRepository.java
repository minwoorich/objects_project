package com.objects.marketbridge.domain.product.infra;

import com.objects.marketbridge.common.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
}
