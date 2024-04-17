package com.objects.marketbridge.domains.coupon.infra;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.domain.QCoupon;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.objects.marketbridge.domains.coupon.domain.QCoupon.coupon;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CouponRepositoryQueryDslImpl {

    private final JPAQueryFactory jpaQueryFactory;

}
