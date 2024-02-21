package com.objects.marketbridge.domains.order.service.port;

import com.objects.marketbridge.domains.member.domain.Address;

import java.util.List;

public interface AddressRepository {

    Address findById(Long id);

    List<Address> findByMemberId(Long memberId);

    void save(Address address);

    Address findAddressValueByAddressId(Long addressId ,Long memberId);

    void deleteById(Long addressId);

    void saveAll(List<Address> addresses);

    void deleteAllInBatch();
}
