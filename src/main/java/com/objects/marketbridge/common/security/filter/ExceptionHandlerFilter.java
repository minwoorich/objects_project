package com.objects.marketbridge.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.security.dto.ErrRes;
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

import static com.objects.marketbridge.common.security.constants.SecurityErrConst.SIGN_IN_ERR;
import static org.springframework.util.MimeTypeUtils.*;


@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }  catch (InternalAuthenticationServiceException | BadCredentialsException e) {
            setErrRes(request, response, HttpStatus.UNAUTHORIZED, SIGN_IN_ERR);
        } catch(Exception e) {
            setErrRes(request, response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void setErrRes(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus httpStatus,
            String message
    ) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON+"; "+"charset=UTF-8");

        response.getWriter().write(
                ErrRes.of(httpStatus, message, request.getRequestURI()).convertToJson()
        );
    }
}
