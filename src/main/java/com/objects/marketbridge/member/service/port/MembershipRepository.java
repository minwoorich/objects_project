package com.objects.marketbridge.member.service.port;

import com.objects.marketbridge.common.domain.Membership;

public interface MembershipRepository {

    Membership save(Membership membership);

    Membership findBySubsOrderNo(String subsOrderNo);

}
