package com.example.backend.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import jakarta.servlet.FilterChain;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    AuthenticationFilter(final RequestMatcher requiresAuth, AuthenticationManager am) {

        super(requiresAuth);
        this.setAuthenticationManager(am);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws AuthenticationException,
            IOException, ServletException {

        String token= httpServletRequest.getHeader(AUTHORIZATION);
        System.out.println(token);
        if (token != null) {
            token = StringUtils.removeStart(token, "Bearer").trim();
        }
        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(requestAuthentication);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain chain, final Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
