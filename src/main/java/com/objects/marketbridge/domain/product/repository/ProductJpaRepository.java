package com.objects.marketbridge.domain.product.repository;

import com.objects.marketbridge.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product,Long> {
}
