package com.objects.marketbridge.common.security.jwt;

import com.objects.marketbridge.common.security.service.JwtTokenProvider;
import com.objects.marketbridge.common.security.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    JwtTokenService jwtTokenService;

    @Test
    public void generatedToken() {


    }
//    @Test
//    public void calcRemainTime() {
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOCIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNzA1NTY5Mzk0LCJleHAiOjEyMH0.9vDIiRbLj6sXNSAVZ3Trr0hAGIqBE15ndG0RZx6G__E";
//        Claims claims = jwtTokenProvider.parseClaims(token);
//
//        long issuedAt = (claims.getIssuedAt()).getTime();
//        long expiration = (claims.getExpiration()).getTime();
//
//        Instant expirationInstant = new Date(issuedAt+expiration).toInstant();
//        Instant now = Instant.now();
//        Duration between = Duration.between(now, expirationInstant);
//
//        log.info("issuedAt={}, expiration={}", issuedAt, expiration);
//        log.info("expirationInstant={}", expirationInstant);
//        log.info("now={}, between={}", now.toEpochMilli(), between.toMillis());
//
//
//    }

}