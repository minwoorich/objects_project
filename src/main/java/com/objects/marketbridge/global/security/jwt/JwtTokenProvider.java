package com.objects.marketbridge.global.security.jwt;

import com.objects.marketbridge.global.security.user.CustomUserDetails;
import com.objects.marketbridge.global.security.jwt.constants.JwtConst;
import com.objects.marketbridge.global.security.jwt.constants.JwtErrConst;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Spring security와 JWT 토큰을 사용하여 인증과 권한 부여를 처리하는 클래스
 * @Component 스프링 빈으로 자동 등록
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final Key KEY;

    /**
     * @param secretKey @Value 어노테이션을 통해 yml파일의 jwt sercet을 가져온다.
     */
    public JwtTokenProvider(@Value(JwtConst.SECRET_KEY) String secretKey) {
        // base64 문자열을 byte 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        // jjwt라이브러리의 Keys 클래스는 토큰 서명 및 검증에 필요한 키를 생성하는 다양한 메서드를 제공함.
        // hmacShaKeyFor 메서드는 HMAC SHA-256키를 생성 함.
        this.KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Member 정보를 가지고 AccessToken과 RefreshToken을 생성함.
     */
    public JwtToken generateToken(Authentication authentication) {

        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        String role = getAuthoritiesAsString(authentication);
        String accessToken = issueToken(memberId, role, JwtConst.ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = issueToken(memberId, role, JwtConst.REFRESH_TOKEN_EXPIRE_TIME);

        // .grantType("Bearer") - OAuth 2.0 방식의 토큰으로 설정
        return JwtToken.builder()
                .grantType(JwtConst.BEARER)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 사용자의 권한 문자열을 가져오는 메서드.
     * @param authentication Authentication 인터페이스는 사용자의 신원 정보와 권한 정보를 가지고 있음.
     * @return JwtToken
     */
    private String getAuthoritiesAsString(Authentication authentication) {
        // authentication.getAuthorities()는 부여된 권한 목록을 반환. GrantedAuthority의 컬렉션.
        // GrantedAuthority는 권한 정보를 나타내는 인터페이스
        // getAuthority() 메서드는 권한을 나타내는 문자열을 반환.
        // --> Spring security의 User객체(UserDetails의 구현체)의 roles 메서드에 의해 ROLE_ prefix가 없을 경우 붙여 줌. "USER -> ROLE_USER"
        // ----> UserDetails는 사용자정보를 나타내는 인터페이스
        // ----> User는 UserDetails의 구현체로 사용자의 계정, 비밀번호 만료나, 계정이 잠겨있는지에 대한 정보를 가지고 있음.
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * 토큰 만료시간을 설정하는 메서드
     * @param hour 현재시간으로 부터 몇 시간 후 만료 될 지. hour 단위
     * @return long - millisecound로 반환
     */
    private long setExpirationTime(int hour) {
        ZoneId systemZone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(hour).atZone(systemZone).toInstant().toEpochMilli();
    }

    /**
     * 목적에 맞는 토근을 발급하는 메서드
     * @param memberId jwt subject
     * @param role 권한
     * @param hour 만료 시간
     * @return string으로 변환된 토큰 발급
     */
    private String issueToken(Long memberId, String role, int hour) {
        // setExpiration의 내부 구현이 Date 객체로 되어 있음.
        long expiresIn = setExpirationTime(hour);

        // .setSubject() jwt의 주제(주로 name)
        // .claim() token 안에 포함되는 정보 - iss(발급자), exp(만료시간), sub(주제) 등등... 이외에도 name, role 등이 있음.
        // .setExpiration() 만료시간
        // .signWith() JWT 서명에 사용되는 비밀키
        // .compact() JWT를 문자열로 반환
        return Jwts.builder()
                .setSubject(memberId.toString())
                .claim(JwtConst.AUTH, role)
                .setExpiration(new Date(expiresIn))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰으로 사용자 정보 반환
     * @param token 유효성 검증이 통과된 토큰
     * @return Authentication 인터페이스는 사용자의 신원 정보와 권한 정보를 가지고 있음.
     */
    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);
        // claims.get(JwtConst.AUTH) 토큰 정보에 있는 권한 리스트를 가져온다.
        if (claims.get(JwtConst.AUTH) == null) {
            throw new RuntimeException(JwtErrConst.AUTHENTICATION_TOKEN_ERR);

        }
        // claim에서 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities = extractAuthoritiesFromToken(claims);
        List<String> roles = new ArrayList<>(Arrays.asList(claims.get(JwtConst.AUTH, String.class).split(",")));
        Long memberId = Long.parseLong(claims.getSubject());
        CustomUserDetails principal = new CustomUserDetails(memberId, null,null, roles);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰에서 권한정보를 추출하여 권한 문자열 컬렉션으로 만드는 메서드
     * @param claims 토큰의 정보
     * @return GrantedAuthority 권한 정보 컬렉션
     */
    private Collection<? extends GrantedAuthority> extractAuthoritiesFromToken(Claims claims) {
        return Arrays.stream(claims.get(JwtConst.AUTH).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 토큰의 유효성을 검증하는 메서드
     * @param token 토큰
     * @return boolean
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info(JwtErrConst.INVALID_TOKEN_ERR, e);
        } catch (ExpiredJwtException e) {
            log.info(JwtErrConst.EXPIRED_TOKEN_ERR, e);
        } catch (UnsupportedJwtException e) {
            log.info(JwtErrConst.UNSUPPORTED_TOKEN_ERR, e);
        } catch (IllegalStateException e) {
            log.info(JwtErrConst.EMPTY_TOKEN_ERR, e);
        }

        return false;
    }

    /**
     * 토큰을 복호화(해독)하여 claim(토큰정보)를 추출
     * @param token 토큰
     * @return Claims 토큰 정보 객체
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
