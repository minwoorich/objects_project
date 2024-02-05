package com.objects.marketbridge.member.infra;

import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    public Member findByEmail(String email){
        return memberJpaRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Member findById(Long id) {
        return memberJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<Member> findOptionalByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Override
    public Member findByIdWithAddresses(Long id) {
        return memberJpaRepository.findByIdWithAddresses(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Member> saveAll(List<Member> members) {
        return memberJpaRepository.saveAll(members);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public void deleteAllInBatch() {
        memberJpaRepository.deleteAllInBatch();
    }
}
