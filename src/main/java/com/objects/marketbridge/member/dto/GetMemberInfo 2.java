package com.objects.marketbridge.member.dto;

import lombok.Builder;

@Builder
public record GetMemberInfo(String email,
                            String name,
                            String phoneNo,
                            Boolean isAgree,
                            Boolean isAlert) {
}
