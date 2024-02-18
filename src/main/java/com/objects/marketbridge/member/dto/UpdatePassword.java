package com.objects.marketbridge.member.dto;

import lombok.Builder;

@Builder
public record UpdatePassword(Long memberId, String password) {
}
