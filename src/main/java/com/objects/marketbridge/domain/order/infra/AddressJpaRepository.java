package com.objects.marketbridge.domain.order.infra;

import com.objects.marketbridge.common.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressJpaRepository extends JpaRepository<Address, Long> {
    List<Address> findByMemberId(Long memberId);
}
