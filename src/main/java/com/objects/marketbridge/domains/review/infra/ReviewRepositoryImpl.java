package com.objects.marketbridge.domains.review.infra;

import com.objects.marketbridge.domains.review.domain.Review;
import com.objects.marketbridge.domains.review.service.port.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    @PersistenceContext
    private final EntityManager em;
    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public void save(Review review) {
        reviewJpaRepository.save(review);
    }

    @Override
    public Review findById(Long id) {
        return reviewJpaRepository.findById(id)
                .orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    @Override
    public void delete(Review review) {
        reviewJpaRepository.delete(review);
    }

//    //LIKE관련//
//    @Override
//    public Page<Review> findAllByProductIdOrderByLikesDesc(Long productId, Pageable pageable) {
//        return reviewJpaRepository.findAllByProductIdOrderByLikesDesc(productId, pageable);
//    }
//
//    //LIKE관련//
//    @Override
//    public Page<Review> findAllByMemberIdOrderByLikesDesc(Long memberId, Pageable pageable) {
//        return reviewJpaRepository.findAllByMemberIdOrderByLikesDesc(memberId, pageable);
//    }

    @Override
    public Page<Review> findAllByProductId(Long productId, Pageable pageable) {
        return reviewJpaRepository.findAllByProductId(productId, pageable);
    }

    @Override
    public Page<Review> findAllByMemberId(Long memberId, Pageable pageable) {
        return reviewJpaRepository.findAllByMemberId(memberId, pageable);
    }

    public Long countByProductId(Long productId) {
        return reviewJpaRepository.countByProductId(productId);
    }

    public Long countByMemberId(Long memberId) {
        return reviewJpaRepository.countByMemberId(memberId);
    }

    @Override
    public Page<Review> findAllByIdIn(List<Long> likedOrderedReviewIds, Pageable pageable) {
        return reviewJpaRepository.findAllByIdIn(likedOrderedReviewIds, pageable);
    }

    @Override
    public Page<Review> findAllByMemberIdOrderByLikesDesc(Long memberId, Pageable pageable) {
        String sql = "SELECT r.* " +
                "FROM review r " +
                "LEFT JOIN ( " +
                "    SELECT review_id, COUNT(*) as like_count " +
                "    FROM review_like " +
                "    GROUP BY review_id " +
                ") rl ON r.review_id = rl.review_id " +
                "WHERE r.member_id = :memberId " +
                "ORDER BY rl.like_count DESC";

        Query query = em.createNativeQuery(sql, Review.class);
        query.setParameter("memberId", memberId);

        // 페이징 적용
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Review> resultList = query.getResultList();

        // 전체 개수를 가져오기 위한 쿼리
        String countSql = "SELECT COUNT(*) " +
                "FROM review r " +
                "LEFT JOIN review_like rl ON r.review_id = rl.review_id " +
                "WHERE r.member_id = :memberId";

        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter("memberId", memberId);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<Review> findAllByProductIdOrderByLikesDesc(Long productId, Pageable pageable) {
        String sql = "SELECT r.* " +
                "FROM review r " +
                "LEFT JOIN ( " +
                "    SELECT review_id, COUNT(*) as like_count " +
                "    FROM review_like " +
                "    GROUP BY review_id " +
                ") rl ON r.review_id = rl.review_id " +
                "WHERE r.productId = :productId " +
                "ORDER BY rl.like_count DESC";

        Query query = em.createNativeQuery(sql, Review.class);
        query.setParameter("productId", productId);

        // 페이징 적용
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Review> resultList = query.getResultList();

        // 전체 개수를 가져오기 위한 쿼리
        String countSql = "SELECT COUNT(*) " +
                "FROM review r " +
                "LEFT JOIN review_like rl ON r.review_id = rl.review_id " +
                "WHERE r.product_id = :productId";

        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter("productId", productId);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public void deleteById(Long reviewId) {
        reviewJpaRepository.deleteById(reviewId);
    }
}
