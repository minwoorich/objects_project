package com.objects.marketbridge.domain.product.infra;

import com.objects.marketbridge.common.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
}
