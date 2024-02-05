package com.objects.marketbridge.product.infra.option;

import com.objects.marketbridge.product.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionJpaRepository extends JpaRepository<Option, Long> {

    Option findByName(String name);
}
