package com.objects.marketbridge.domains.member.service.port;

import com.objects.marketbridge.domains.member.domain.Membership;

public interface MembershipRepository {

    Membership save(Membership membership);

    Membership findById(Long id);

    Membership findBySubsOrderNo(String subsOrderNo);

}
