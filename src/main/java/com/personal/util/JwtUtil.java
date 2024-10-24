package com.personal.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class JwtUtil {
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Use a secure key in production

    // Generate token with username and customerId
    public String generateToken(String username, String customerId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("customerId", customerId);  // Add customerId to the token payload
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)  // Set username as the subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate the token with username and customerId
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract customerId from token
    public String extractCustomerId(String token) {
        return extractAllClaims(token).get("customerId", String.class);  // Extract customerId from the claims
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()  // Build the parser before parsing
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
