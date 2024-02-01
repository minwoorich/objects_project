package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionJpaRepository extends JpaRepository<Option, Long> {

    Optional<Option> findById(Long id);
    Optional<Option> findByName(String name);
}
