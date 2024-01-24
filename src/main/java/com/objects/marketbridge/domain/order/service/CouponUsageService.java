package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.coupon.repository.MemberCouponRepository;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponUsageService {

    public void applyCouponUsage(MemberCouponRepository memberCouponRepository, List<OrderDetail> orderDetails, Long memberId) {
        orderDetails.stream()
                .filter(o -> o.getCoupon() != null)
                .map(o ->
                        memberCouponRepository.findByMember_IdAndCoupon_Id(
                                memberId,
                                o.getCoupon().getId())
                )
                .forEach(mc ->
                        mc.applyCouponUsage(true, LocalDateTime.now()));
    }

}
