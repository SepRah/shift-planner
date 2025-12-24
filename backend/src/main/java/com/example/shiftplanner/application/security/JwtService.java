package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * Service responsible for creating, parsing and validating JWT tokens.
 * It is used for stateless authentication between client and backend.
 */
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Generates a signed JWT token for an authenticated user.
     *
     * @param username the user's unique identifier
     * @param roles    the user's roles (AUTHORITIES)
     * @return signed JWT token
     */
    public String generateToken(String username, Collection<UserRole> roles) {

        List<String> roleNames = roles.stream()
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     * @param token The jwt token
     * @return the username
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Checks if token:
     * 1) belongs to the given user
     * 2) is not expired
     * @param token The jwt token
     * @param user The user object
     * @return whether the token for the user is valid
     */
    public boolean isTokenValid(String token, User user) {
        return user.getUsername().equals(extractUsername(token))
                && !isExpired(token);
    }

    /**
     * Checks whether the token is expired.
     * @param token The jwt token
     * @return whether the token is expired
     */
    private boolean isExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Parses the token and extracts all claims.
     * Signature is verified automatically using the signing key.
     * @param token The jwt token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Creates the cryptographic signing key from the secret.
     * Used both for signing and verifying JWTs.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}

