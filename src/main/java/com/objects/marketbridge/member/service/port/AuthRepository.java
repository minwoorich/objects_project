package com.objects.marketbridge.member.service.port;

import com.objects.marketbridge.member.dto.AuthMember;

public interface AuthRepository {
    AuthMember findAuthMemberByEmail(String email);
}
