package com.objects.marketbridge.product.infra.option;

import com.objects.marketbridge.product.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionJpaRepository extends JpaRepository<Option, Long> {

    Optional<Option> findByName(String name);
}
