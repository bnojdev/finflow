package com.binoj.finflow.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        log.debug("Authorization header: {}", header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            log.debug("Extracted token: {}", token);

            if (jwtUtil.validateToken(token)) {
                String mobile = jwtUtil.extractMobile(token);
                log.info("Valid token for mobile: {}", mobile);

                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(mobile, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Invalid token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            log.debug("No Authorization header or not Bearer");
        }

        filterChain.doFilter(request, response);
    }
}