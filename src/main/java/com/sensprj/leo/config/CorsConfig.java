package com.sensprj.leo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Frontend Origins (lokal + Docker)
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174"
        ));

        // Methoden erlauben (wichtig für Preflight/OPTIONS)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Welche Header der Browser senden darf
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));


        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
