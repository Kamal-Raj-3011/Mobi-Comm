package com.mobicomm.app.root.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

	 private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("your-very-secure-secret-key-your-very-secure-secret-key".getBytes());

	    public String generateToken(String email, int expiryMinutes) {
	        return Jwts.builder()
	                .setSubject(email)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000))
	                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // ✅ FIXED: Uses SecretKey
	                .compact();
	    }

    // Extract email from token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract any claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder() // ✅ Use parserBuilder() instead of parser()
                .setSigningKey(SECRET_KEY) // ✅ Use SecretKey
                .build() // ✅ Required in new versions
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate token
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Invalid or tampered token
        }
    }
}
