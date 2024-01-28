package com.objects.marketbridge.domain.member.infra;

import com.objects.marketbridge.common.domain.Member;

public interface MemberCustomRepository {
    //JPA가 지원하지 않는 기능

    Member findByIdWithPointAndAddresses(Long id);

}
