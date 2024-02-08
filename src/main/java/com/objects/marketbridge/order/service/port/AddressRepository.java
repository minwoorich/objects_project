package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.member.domain.Address;

import java.util.List;

public interface AddressRepository {

    Address findById(Long id);

    List<Address> findByMemberId(Long memberId);

    void save(Address address);

    void saveAll(List<Address> addresses);

    void deleteAllInBatch();
}
