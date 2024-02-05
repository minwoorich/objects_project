package com.objects.marketbridge.member.infra;

import com.objects.marketbridge.member.domain.Membership;
import com.objects.marketbridge.member.service.port.MembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepository {

    private final MembershipJpaRepository membershipJpaRepository;

    @Override
    public Membership save(Membership membership) {
        return membershipJpaRepository.save(membership);
    }

    @Override
    public Membership findById(Long id) {
        return membershipJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }

    @Override
    public Membership findBySubsOrderNo(String subsOrderNo) {
        return membershipJpaRepository.findBySubsOrderNo(subsOrderNo).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }
}
