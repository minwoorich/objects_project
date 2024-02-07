package com.objects.marketbridge.product.infra.option;

import com.objects.marketbridge.product.domain.OptionCategory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OptionCategoryJpaRepository extends JpaRepository<OptionCategory,Long> {

    Optional<OptionCategory> findByName(String name);
}
