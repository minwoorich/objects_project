package com.objects.marketbridge.member.service.port;

import com.objects.marketbridge.common.domain.Member;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository {

    Member findByEmail(String email);
    Optional<Member> findOptionalByEmail(String email);
    Member findById(Long id);

    Member save(Member member);

    Member findByIdWithAddresses(Long id);

    void deleteAllInBatch();
}
