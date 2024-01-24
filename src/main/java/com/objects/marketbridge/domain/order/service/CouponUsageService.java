package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.model.MemberCoupon;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CouponUsageService {

    public void applyCouponUsage(List<MemberCoupon> memberCoupons, Boolean isUsed, LocalDateTime usedDate) {

        memberCoupons.forEach(mc -> mc.applyCouponUsage(isUsed, usedDate));
    }

}
