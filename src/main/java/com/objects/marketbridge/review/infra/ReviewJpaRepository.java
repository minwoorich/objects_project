package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.common.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
}
