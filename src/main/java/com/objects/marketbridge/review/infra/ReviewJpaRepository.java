package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.product.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
}
