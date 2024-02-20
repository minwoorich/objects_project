package com.objects.marketbridge.review.infra;

import com.objects.marketbridge.review.domain.ReviewImage;
import com.objects.marketbridge.review.service.port.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewImageRepositoryImpl implements ReviewImageRepository {

    private final ReviewImageJpaRepository reviewImageJpaRepository;

    @Override
    public void save(ReviewImage reviewImage) {
        reviewImageJpaRepository.save(reviewImage);
    }

    @Override
    public List<ReviewImage> findAllByReviewId(Long reviewId) {
        return reviewImageJpaRepository.findAllByReviewId(reviewId);
    }

    @Override
    public void delete(ReviewImage reviewImage) {
        reviewImageJpaRepository.delete(reviewImage);
    }

    @Override
    public void saveAll(List<ReviewImage> reviewImages) {
        reviewImageJpaRepository.saveAll(reviewImages);
    }

    @Override
    public void deleteAll(Iterable<? extends ReviewImage> reviewImages) {
        reviewImageJpaRepository.deleteAll(reviewImages);
    }

    @Override
    public void deleteByReviewId(Long reviewId) {
        reviewImageJpaRepository.deleteByReviewId(reviewId);
    }
}