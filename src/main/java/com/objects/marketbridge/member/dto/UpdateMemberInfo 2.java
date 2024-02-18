package com.objects.marketbridge.member.dto;

import lombok.Builder;

@Builder
public record UpdateMemberInfo(
    String email,
    String name,
    String phoneNo,
    String password,
    Boolean isAgree,
    Boolean isAlert
){}