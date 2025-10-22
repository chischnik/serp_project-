package com.serp.auth;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthController(UserRepository u, PasswordEncoder e, JwtService j){
    this.users = u; this.encoder = e; this.jwt = j;
  }

  public record RegisterReq(@NotBlank String email, @NotBlank String password){}
  public record LoginReq(@NotBlank String email, @NotBlank String password){}
  public record TokenRes(String token){}

  @PostMapping("/register")
  public ResponseEntity<Map<String,Object>> register(@RequestBody RegisterReq req){
    if(users.findByEmail(req.email()).isPresent())
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","email_exists"));
    UserEntity u = new UserEntity();
    u.setEmail(req.email());
    u.setPasswordHash(encoder.encode(req.password()));
    users.save(u);
    String token = jwt.createToken(u.getId(), u.getEmail(), List.of("USER"));
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenRes> login(@RequestBody LoginReq req){
    return users.findByEmail(req.email())
      .filter(u -> encoder.matches(req.password(), u.getPasswordHash()))
      .map(u -> ResponseEntity.ok(new TokenRes(jwt.createToken(u.getId(), u.getEmail(), List.of("USER")))))
      .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @GetMapping("/me")
  public Map<String,Object> me(@AuthenticationPrincipal Jwt principal){
    if(principal == null) return Map.of("authenticated", false);
    return Map.of(
      "authenticated", true,
      "sub", principal.getSubject(),
      "username", principal.getClaimAsString("username"),
      "roles", principal.getClaimAsStringList("roles")
    );
  }
}
