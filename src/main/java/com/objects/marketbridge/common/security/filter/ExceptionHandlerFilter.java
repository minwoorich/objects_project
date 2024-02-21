package com.objects.marketbridge.common.security.filter;

import com.objects.marketbridge.common.security.dto.ErrRes;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;


@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }  catch (InternalAuthenticationServiceException |
                  BadCredentialsException |
                  ExpiredJwtException |
                  SecurityException |
                  MalformedJwtException |
                  UnsupportedJwtException |
                  SignatureException |
                  IllegalStateException e) {
            log.warn("[ExceptionHandler] {} ", e.getMessage());
            log.warn("[ExceptionHandler] {} ", e.getStackTrace()[0]);

            setErrRes(request, response, e.getMessage());
        }
    }

    private void setErrRes(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON+"; "+"charset=UTF-8");

        response.getWriter().write(
                ErrRes.of(HttpStatus.UNAUTHORIZED, message, request.getRequestURI()).convertToJson()
        );
    }
}
