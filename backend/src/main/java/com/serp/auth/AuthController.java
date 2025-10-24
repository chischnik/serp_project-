package com.serp.auth;

import jakarta.validation.Valid;
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

    public record RegisterReq(@NotBlank String email, @NotBlank String password) {}
    public record LoginReq(@NotBlank String email, @NotBlank String password) {}
    public record TokenRes(String token) {}

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody @Valid RegisterReq req){
        if (users.findByEmail(req.email()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "email_exists"));

        var u = new UserEntity();
        u.setEmail(req.email()); // Alias -> username
        u.setPasswordHash(encoder.encode(req.password()));
        users.save(u);

        var token = jwt.createToken(u.getId(), u.getEmail(), List.of("USER"));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@RequestBody @Valid LoginReq req){
        return users.findByEmail(req.email())
                .filter(u -> encoder.matches(req.password(), u.getPasswordHash()))
                .map(u -> new TokenRes(jwt.createToken(u.getId(), u.getEmail(), List.of("USER"))))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt principal){
        if (principal == null) return Map.of("authenticated", false);

        var roles = Optional.ofNullable(principal.getClaimAsStringList("roles")).orElse(List.of());
        var username = Optional.ofNullable(principal.getClaimAsString("username")).orElse("");

        return Map.of(
                "authenticated", true,
                "sub", principal.getSubject(),
                "username", username,
                "roles", roles
        );
    }
}
