package com.objects.marketbridge.domains.review.infra;

import com.objects.marketbridge.domains.order.domain.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailReviewJpaRepository extends JpaRepository<OrderDetail, Long> {

    @Query(value = "SELECT o.member_id, od.* " +
            "FROM order_detail od " +
            "INNER JOIN orders o ON od.order_id = o.order_id " +
            "WHERE o.member_id = :memberId " +
            "ORDER BY delivered_date DESC"
            ,
//            countQuery = "SELECT COUNT(*) FROM order_detail od INNER JOIN orders o ON od.order_id = o.order_id WHERE o.member_id = :memberId",
            nativeQuery = true)
    Page<OrderDetail> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
