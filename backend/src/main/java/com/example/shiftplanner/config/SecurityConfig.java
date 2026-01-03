package com.example.shiftplanner.config;

import com.example.shiftplanner.application.security.AuthEntryPointJwt;
import com.example.shiftplanner.application.security.JwtAuthFilter;
import com.example.shiftplanner.application.security.ShiftplannerUserDetailsService;
import com.example.shiftplanner.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    // --- Password encoder ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- UserDetailsService ---
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // --- Authentication provider ---
    @Bean
    public AuthenticationProvider authenticationProvider(ShiftplannerUserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // --- Authentication manager ---
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // --- Security filter chains ---
    @Bean
    @Order(1)
    @Profile("h2")
    public SecurityFilterChain h2Chain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/h2-console/**")
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    @Profile("dev")
    public SecurityFilterChain swaggerChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    // Public Authentication Endpoints
    @Bean
    @Order(3)
    public SecurityFilterChain authChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/auth/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().permitAll());

        return http.build();
    }

    // JWT + Role-secured API
    @Bean
    @Order(4)
    public SecurityFilterChain apiChain(HttpSecurity http,
                                        JwtAuthFilter jwtAuthFilter,
                                        AuthenticationProvider authProvider) throws Exception {

        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
