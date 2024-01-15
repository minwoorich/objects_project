package com.objects.marketbridge.global.security.jwt;

import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
