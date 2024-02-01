package com.objects.marketbridge.member.infra;

import com.objects.marketbridge.common.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipJpaRepository extends JpaRepository<Membership,Long> {

    Optional<Membership> findBySubsOrderNo(String subsOrderNo);

}
