package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.Review;

public interface ReviewRepository {

    void save(Review review);

    Review findById(Long id);

    void delete(Review review);
}
