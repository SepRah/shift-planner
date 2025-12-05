package com.example.shiftplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // required for stateless APIs
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers("/auth/**").permitAll()

                        // System admin only
                        .requestMatchers("/system/**").hasRole("SYSTEM_ADMIN")

                        // Admin or system admin
                        .requestMatchers("/admin/**")
                        .hasAnyRole("ADMIN", "SYSTEM_ADMIN")

                        // Normal user (or higher)
                        .requestMatchers("/user/**")
                        .hasAnyRole("USER", "ADMIN", "SYSTEM_ADMIN")

                        // Everything else must be authenticated
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // temporary basic auth (until JWT later)

        return http.build();
    }
}
