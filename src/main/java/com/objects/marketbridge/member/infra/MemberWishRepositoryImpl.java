package com.objects.marketbridge.member.infra;

import com.objects.marketbridge.member.service.port.MemberWishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberWishRepositoryImpl implements MemberWishRepository {

    private final MemberWishRepository memberWishRepository;
}
