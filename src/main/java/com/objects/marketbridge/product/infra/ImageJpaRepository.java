package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {

    Optional<Image> findById(Long id);
}
