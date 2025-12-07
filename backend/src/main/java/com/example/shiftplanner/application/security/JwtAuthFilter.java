package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.infrastructure.UserRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        String authHeader = request.getHeader("Authorization");

        // No token provided → continue without authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7); // remove "Bearer "
        String username = jwtService.extractUsername(jwt);

        // Only authenticate if not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByUsername(username)
                    .orElse(null);

            if (user != null && jwtService.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities() // from your UserDetails implementation
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Put user into security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
