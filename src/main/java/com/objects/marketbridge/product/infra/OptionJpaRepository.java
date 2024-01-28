package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionJpaRepository extends JpaRepository<Option, Long> {

    Option findByName(String name);
}
