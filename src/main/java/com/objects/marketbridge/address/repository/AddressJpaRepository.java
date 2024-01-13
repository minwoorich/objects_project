package com.objects.marketbridge.address.repository;

import com.objects.marketbridge.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressJpaRepository extends JpaRepository<Address, Long> {
    Address findByMemberId(Long memberId);
}
