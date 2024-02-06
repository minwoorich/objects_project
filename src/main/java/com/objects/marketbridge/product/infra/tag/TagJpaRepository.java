package com.objects.marketbridge.product.infra.tag;

import com.objects.marketbridge.product.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag,Long> {

    Optional<Tag> findByName(String name);
    Optional<Tag> findById(Long id);
}
