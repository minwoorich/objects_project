package com.objects.marketbridge.address.repository;

import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {

    private final AddressJpaRepository addressJpaRepository;

    @Override
    public Address findById(Long id) {
        return addressJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 엔티티가 존재하지 않습니다"));
    }

    @Override
    public void save(Address address) {
        addressJpaRepository.save(address);
    }

    @Override
    public void saveAll(List<Address> addresses) {
        addressJpaRepository.saveAll(addresses);
    }

    @Override
    public Address findByMemberId(Long memberId) {
        return addressJpaRepository.findByMemberId(memberId);
    }

    @Override
    public void deleteAllInBatch(){
        addressJpaRepository.deleteAllInBatch();
    }
}
