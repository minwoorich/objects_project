package com.objects.marketbridge.image.infra;

import com.objects.marketbridge.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
}
