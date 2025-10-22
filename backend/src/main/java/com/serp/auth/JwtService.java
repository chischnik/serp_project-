package com.serp.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.*;
import java.util.*;

@Service
public class JwtService {
  @Value("${security.jwt.secret}") String secret;
  @Value("${security.jwt.issuer}") String issuer;
  @Value("${security.jwt.expirationMinutes}") long expMin;

  public String createToken(UUID userId, String username, java.util.Collection<String> roles){
    Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
      .setIssuer(issuer)
      .setSubject(userId.toString())
      .claim("username", username)
      .claim("roles", roles)
      .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(expMin))))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }
}
