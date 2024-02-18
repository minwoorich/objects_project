package com.objects.marketbridge.coupon.infra;

import com.objects.marketbridge.coupon.domain.Coupon;
import com.objects.marketbridge.coupon.service.port.CouponRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final JPAQueryFactory queryFactory;

    public CouponRepositoryImpl(CouponJpaRepository couponJpaRepository, EntityManager em) {
        this.couponJpaRepository = couponJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Coupon findById(Long id) {
        return couponJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(Coupon coupon) {
        couponJpaRepository.save(coupon);
    }

    @Override
    public void saveAll(List<Coupon> coupons) {
        couponJpaRepository.saveAll(coupons);
    }

    @Override
    public List<Coupon> findAll() {
        return couponJpaRepository.findAll();
    }
}
