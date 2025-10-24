package com.serp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig {

    @Value("${app.cors.origins:http://localhost:3000}")
    private String corsOrigins;

    /**
     * Bevorzugt: Ein CorsConfigurationSource-Bean.
     * Spring Security greift dieses Bean automatisch auf, wenn http.cors() aktiviert ist.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // exakte Origins erlauben (Credentials sind erlaubt, daher keine Wildcards)
        cfg.setAllowedOrigins(
                Arrays.stream(corsOrigins.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList())
        );

        cfg.addAllowedHeader("*");
        cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowCredentials(true);
        // Optional: Header, die der Browser sehen darf
        cfg.setExposedHeaders(Arrays.asList("Authorization", "Location"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
