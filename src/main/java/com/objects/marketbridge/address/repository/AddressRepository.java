package com.objects.marketbridge.address.repository;

import com.objects.marketbridge.domain.model.Address;

public interface AddressRepository {

    Address findById(Long id);

    Address findByMemberId(Long memberId);

    void save(Address address);

    void deleteAllInBatch();
}
