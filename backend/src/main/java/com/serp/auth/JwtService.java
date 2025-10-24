package com.serp.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private final Key key;
    private final String issuer;
    private final long expMinutes;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer:serp-auth}") String issuer,
            @Value("${security.jwt.expirationMinutes:120}") long expMinutes
    ) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("security.jwt.secret ist zu kurz (mind. 32 Bytes für HS256).");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.expMinutes = expMinutes;
    }

    public String createToken(UUID userId, String username, Collection<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofMinutes(expMinutes));
        List<String> roleList = roles == null ? List.of() : List.copyOf(roles);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(userId.toString())
                .setIssuedAt(Date.from(now))
                .setNotBefore(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("username", username)
                .claim("roles", roleList)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
