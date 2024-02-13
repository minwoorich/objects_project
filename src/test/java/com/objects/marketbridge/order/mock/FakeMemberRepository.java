package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {

    private Long autoGeneratedId = 0L;
    private List<Member> data = new ArrayList<>();

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public Member findByEmail(String email) {
        return null;
    }

    @Override
    public Optional<Member> findOptionalByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Member findById(Long id) {
        return data.stream()
                .filter(member -> member.getId().equals(id))
                .findAny().orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Member save(Member member) {
        if (member.getId() == null || member.getId() == 0) {
            ReflectionTestUtils.setField(member, "id", ++autoGeneratedId, Long.class);
            data.add(member);
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), member.getId()));
            data.add(member);
        }
        return member;
    }

    @Override
    public List<Member> saveAll(List<Member> members) {
        return null;
    }

    @Override
    public Member findByIdWithAddresses(Long id) {
        return null;
    }

    @Override
    public void deleteAllInBatch() {
        data.clear();
        autoGeneratedId = 0L;
    }
}
