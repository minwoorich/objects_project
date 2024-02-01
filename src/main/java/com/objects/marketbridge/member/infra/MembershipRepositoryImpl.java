package com.objects.marketbridge.member.infra;

import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.member.service.port.MembershipRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;


@Repository
public class MembershipRepositoryImpl implements MembershipRepository {

    private final MembershipJpaRepository membershipJpaRepository;
    private final JPAQueryFactory queryFactory;

    public MembershipRepositoryImpl(MembershipJpaRepository membershipJpaRepository, EntityManager em) {
        this.membershipJpaRepository = membershipJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Membership save(Membership membership) {
        return membershipJpaRepository.save(membership);
    }


    @Override
    public Membership findBySubsOrderNo(String subsOrderNo) {
        return membershipJpaRepository.findBySubsOrderNo(subsOrderNo).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }
}
