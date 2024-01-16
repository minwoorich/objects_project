package com.objects.marketbridge.global.security.jwt;

import com.objects.marketbridge.global.security.jwt.constants.JwtConst;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 모든 요청에서 토큰이 있는 지 확인하여 인증 처리
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // resolveToken() request 객체에서 token을 추출
        String token = resolveToken(request);

        // 토큰이 존재하고, 검증상태가 유효하면
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // token을 해석하여 사용자 정보를 가져옴
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // spring security에서 현재 스레드의 보안 컨텍스트에 새로운 인증 정보를 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


    /**
     * request의 Header의 Authorization에서 bearerToken을 가져와 bearer prefix를 제거하고 토큰만 반환
     * @param request 인증이 필요한 API 요청
     * @return String
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtConst.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConst.BEARER)) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}
