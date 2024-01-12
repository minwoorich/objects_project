package com.objects.marketbridge.domain.member.repository;

import com.objects.marketbridge.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    //JpaRepository에서 제공되는 기본메서드 사용
    Optional<Member> findByEmail(String email);

}
