package com.objects.marketbridge.address.repository;

import com.objects.marketbridge.domain.model.Address;

public interface AddressRepository {

    Address findById(Long id);

    void save(Address address);
}
