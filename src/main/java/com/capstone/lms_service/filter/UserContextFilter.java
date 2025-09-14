package com.capstone.lms_service.filter;

import com.capstone.lms_service.exception.HeaderException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip authentication for health endpoint
        if (path.startsWith("/actuator/health")) {
            filterChain.doFilter(request, response);
            return;
        }
        // fetch  user info from header
        String userId = request.getHeader("X-User-Id");
        String userEmail = request.getHeader("X-User-Email");
        String userRoles = request.getHeader("X-User-Role");

        log.info("Extracted headers - UserId: {}, UserEmail: {}, UserRoles: {}",
                userId, userEmail, userRoles);

        if (userId != null && !userId.trim().isEmpty() &&
                userRoles != null && !userRoles.trim().isEmpty()) {

            List<SimpleGrantedAuthority> authorities = Arrays.stream(userRoles.split(","))
                    .map(String::trim)
                    .filter(role -> !role.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("User authenticated successfully: {} with roles: {}", userId, authorities);
        } else {
            log.warn("Authentication failed - Missing or empty userId ({}) or userRoles ({})", userId, userRoles);
            throw new HeaderException("Authentication failed!");
        }

        filterChain.doFilter(request, response);
        log.info("UserContextFilter completed for path: {}", request.getRequestURI());
    }
}