package com.objects.marketbridge.domains.order.infra.address;

import com.objects.marketbridge.domains.member.domain.Address;
import com.objects.marketbridge.domains.order.service.port.AddressRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {

    private final AddressJpaRepository addressJpaRepository;

    @Override
    public Address findById(Long id) {
        return addressJpaRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    @Override
    public Address findAddressValueByAddressId(Long addressId, Long memberId) {
        return addressJpaRepository.findAddressValueByAddressId(addressId,memberId);
    }

    @Override
    public void save(Address address) {
        addressJpaRepository.save(address);
    }

    @Override
    public void deleteById(Long id) {
        addressJpaRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<Address> addresses) {
        addressJpaRepository.saveAll(addresses);
    }

    @Override
    public List<Address> findByMemberId(Long memberId) {
        return addressJpaRepository.findByMemberId(memberId);
    }

    @Override
    public void deleteAllInBatch(){
        addressJpaRepository.deleteAllInBatch();
    }
}
