package com.serp.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration @EnableMethodSecurity
public class SecurityConfig {
  @Value("${security.jwt.secret}") String secret;

  @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean JwtDecoder jwtDecoder(){
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return token -> {
      var parser = io.jsonwebtoken.Jwts.parserBuilder().setSigningKey(key).build();
      var jws = parser.parseClaimsJws(token);
      return new Jwt(token, null, null, java.util.Map.of(), jws.getBody());
    };
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf->csrf.disable())
       .authorizeHttpRequests(reg->reg
         .requestMatchers("/auth/**").permitAll()
         .anyRequest().authenticated()
       )
       .oauth2ResourceServer(o->o.jwt());
    return http.build();
  }
}
