package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalcTotalDiscountService{

    public Long calculate(Order order) {

        Long totalCouponPrice = calcUsedCoupon(order.getOrderDetails());
        Long totalUsedPoint = calcUsedPoint(order.getOrderDetails());
        Long memberShipDiscount = calcMembershipDiscount();

        return totalCouponPrice + totalUsedPoint + memberShipDiscount;
    }

    private Long calcUsedCoupon(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .filter(o -> o.getCoupon() != null)
                .mapToLong(o -> o.getCoupon().getPrice())
                .sum();
    }

    // 추후에 point 서비스 도입하면 이것도 구현 필요
    private Long calcUsedPoint(List<OrderDetail> orderDetails) {
        return 0L;
    }

    // 추후에 membership 할인 서비스 도입하면 이것도 구현 필요
    private Long calcMembershipDiscount() {
        return 0L;
    }

}
