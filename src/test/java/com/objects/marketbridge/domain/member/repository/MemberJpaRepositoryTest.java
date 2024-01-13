package com.objects.marketbridge.domain.member.repository;

import com.objects.marketbridge.address.repository.AddressRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.AddressValue;
import com.objects.marketbridge.domain.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired AddressRepository addressRepository;

    @Test
    void findByIdWithAddresses() {
        // given
        Member member = createMember("홍길동");
        memberJpaRepository.save(member);

        Address address1 = createAddress("서울", member);
        Address address2 = createAddress("부산", member);

        address1.setMember(member);
        address2.setMember(member);
        addressRepository.saveAll(List.of(address1, address2));

        List<String> expectedCities = List.of("서울", "부산");

        //when
        Member memberWithAddresses = memberJpaRepository.findByIdWithAddresses(member.getId());
        List<Address> addresses = memberWithAddresses.getAddresses();

        //then
        assertThat(memberWithAddresses.getName()).isEqualTo("홍길동");
        assertThat(addresses).hasSize(2);
        List<String> actualCities = addresses.stream().map(address -> address.getAddressValue().getCity()).toList();

        assertThat(actualCities).containsExactlyInAnyOrderElementsOf(expectedCities);


    }

    private static Address createAddress(String city, Member member) {
        return Address.builder()
                .addressValue(AddressValue.builder()
                        .city(city)
                        .build())
                .member(member)
                .build();
    }

    private static Member createMember(String name) {
        return Member.builder()
                .name(name)
                .build();
    }
}