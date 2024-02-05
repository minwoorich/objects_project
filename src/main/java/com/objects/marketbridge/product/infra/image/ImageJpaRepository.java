package com.objects.marketbridge.product.infra.image;

import com.objects.marketbridge.member.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
}
