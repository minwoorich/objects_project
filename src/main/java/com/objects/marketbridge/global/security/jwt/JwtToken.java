package com.objects.marketbridge.global.security.jwt;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "JwtToken", timeToLive = 7 * 24 * 60 * 60)
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken {
    @Id
    private String id;

    private String grantType;
    private String accessToken;

    @Indexed
    private String refreshToken;

    @Builder
    public JwtToken(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
