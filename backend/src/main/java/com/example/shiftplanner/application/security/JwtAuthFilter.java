package com.example.shiftplanner.application.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashSet;
import java.util.Set;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, AuthEntryPointJwt unauthorizedHandler) {
        this.jwtService = jwtService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    /**
     * Performs filtering logic for each HTTP request processed by the application.
     * <p>
     * This method is invoked once per request within a single request thread and
     * allows pre-processing and/or post-processing of the request and response
     * before delegating to the next filter in the chain.
     *
     * @param request      the current HTTP request
     * @param response     the current HTTP response
     * @param filterChain  the filter chain to pass control to the next filter
     * @throws ServletException if an error occurs during request processing
     * @throws IOException      if an I/O error occurs during request or response handling
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        String path = request.getServletPath();
        // Endpoints that should skip JWT
        if (path.startsWith("/h2-console") ||
                path.startsWith("/auth/login") ||
                path.startsWith("/auth/register"))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // No token provided → continue without authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7); // remove "Bearer "
        String username;
        try {
            username = jwtService.extractUsername(jwt);
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            unauthorizedHandler.commence(
                    request,
                    response,
                    new org.springframework.security.authentication.InsufficientAuthenticationException("JWT expired or invalid", e)
            );
            // stop the chain
            return;
        }

        // Only authenticate if not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtService.isTokenValid(jwt)) {

                Set<GrantedAuthority> authorities = new HashSet<>();

                // System roles
                jwtService.extractSystemRoles(jwt).forEach(role ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()))
                );

                // Staff qualifications
                jwtService.extractStaffQualifications(jwt).forEach(staff ->
                        authorities.add(new SimpleGrantedAuthority("STAFF_" + staff))
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
