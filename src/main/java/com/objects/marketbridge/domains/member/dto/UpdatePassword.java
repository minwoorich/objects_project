package com.objects.marketbridge.domains.member.dto;

import lombok.Builder;

@Builder
public record UpdatePassword(Long memberId, String password) {
}
