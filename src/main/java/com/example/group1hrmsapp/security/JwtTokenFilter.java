package com.example.group1hrmsapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey; // Your secret key used for token signing

    public JwtTokenFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        // Extract the JWT token from the Authorization header
        String token = extractTokenFromHeader(request.getHeader("Authorization"));

        // Validate and parse the JWT token
        if (token != null && validateToken(token)) {
            // Token is valid, so proceed with the request
            filterChain.doFilter(request, response);
        } else {
            // Token is invalid or not present, return unauthorized response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String extractTokenFromHeader(String header) {
        // Extract the token part from the header (Bearer <token>)
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            // Parse the token and verify its signature with the secret key
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
