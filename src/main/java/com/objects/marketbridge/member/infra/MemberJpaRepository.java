package com.objects.marketbridge.member.infra;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.dto.AuthMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//JpaRepository에서 제공되는 기본메서드 사용
public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberById(Long id);

    @Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.addresses WHERE m.id = :memberId")
    Optional<Member> findByIdWithAddresses(@Param("memberId") Long id);
    
    Optional<AuthMember> findAuthMemberByEmail(String email);

}
