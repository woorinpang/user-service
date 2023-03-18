package com.woorinpang.userservice.global.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        JwtException exception = getJwtException(request);
        if (exception != JwtException.EMPTY_TOKEN) exception.setResponse(response);
    }

    private JwtException getJwtException(HttpServletRequest request) {
        String exception = (String) request.getAttribute("exception");
        return exception == null ? JwtException.EMPTY_TOKEN : JwtException.valueOf(exception);
    }
}
