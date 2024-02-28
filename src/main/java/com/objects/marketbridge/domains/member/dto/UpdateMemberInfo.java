package com.objects.marketbridge.domains.member.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UpdateMemberInfo(
    @NotEmpty
    @Email
    String email,

    @NotEmpty
    String name,

    @NotEmpty
    @Size(min = 11)
    String phoneNo,

    @NotEmpty
    @Size(min = 4)
    String password,

    @NotNull
    Boolean isAgree,

    @NotNull
    Boolean isAlert
) {
}