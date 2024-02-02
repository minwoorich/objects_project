package com.objects.marketbridge.review.service.port;

import com.objects.marketbridge.common.domain.Review;

public interface ReviewRepository {

    void save(Review review);

    Review findById(Long id);

    void delete(Review review);
}
