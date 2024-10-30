package com.example.easybankproject.utiltests;

import com.example.easybankproject.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String token;
    private String username = "testuser";

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        token = jwtUtil.generateToken(username);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testValidateToken() {
        assertTrue(jwtUtil.validateToken(token, username));
    }

    @Test
    public void testExtractUsername() {
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testIsTokenExpired() {
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    public void testExtractAllClaims() {
        Claims claims = jwtUtil.extractAllClaims(token);
        assertNotNull(claims);
        assertEquals(username, claims.getSubject());
    }
}