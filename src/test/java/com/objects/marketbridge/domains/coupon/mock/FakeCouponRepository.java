package com.objects.marketbridge.domains.coupon.mock;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FakeCouponRepository extends BaseFakeCouponRepository implements CouponRepository {
    @Override
    public Coupon findById(Long id) {
        return getInstance().getData().stream()
                .filter(item -> item.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException("해당 엔티티가 존재하지 않습니다. 입력 id = "+id)));
    }

    @Override
    public Coupon save(Coupon coupon) {
        if (coupon.getId() == null || coupon.getId() == 0) {
            ReflectionTestUtils.setField(coupon, "id", increaseId(), Long.class);
            getInstance().getData().add(coupon);
        } else {
            getInstance().getData().removeIf(item -> Objects.equals(item.getId(), coupon.getId()));
            getInstance().getData().add(coupon);
        }
        return coupon;
    }

    @Override
    public List<Coupon> findByProductId(Long productId) {
        return getInstance().getData().stream()
                .filter(coupon -> coupon.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Coupon> coupons) {
        coupons.forEach(this::save);
    }

    @Override
    public Coupon saveAndFlush(Coupon coupon) {
        return null;
    }

    @Override
    public List<Coupon> findAll() {
        return getInstance().getData();
    }

    @Override
    public void deleteAllInBatch() {

    }
}
