package com.objects.marketbridge.order.infra.address;

import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.order.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressJpaRepository extends JpaRepository<Address, Long> {
    List<Address> findByMemberId(Long memberId);

    @Query("SELECT DISTINCT a FROM Address a WHERE a.id = :addressId and a.member = :memberId")
    Address findAddressValueByAddressId(Long addressId, Long memberId);
}
