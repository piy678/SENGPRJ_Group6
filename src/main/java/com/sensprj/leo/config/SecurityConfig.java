package com.sensprj.leo.config;

import com.sensprj.leo.security.AuditAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final AuditAccessDeniedHandler deniedHandler;

    public SecurityConfig(AuditAccessDeniedHandler deniedHandler) {
        this.deniedHandler = deniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.accessDeniedHandler(deniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Teacher-only: Änderungen an assessments
                        .requestMatchers(HttpMethod.POST,  "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT,   "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE,"/api/assessments/**").hasRole("TEACHER")

                        // alles andere: eingeloggt
                        .anyRequest().authenticated()
                )
                // zum Testen erstmal aktiv lassen (sonst 401)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://13.53.169.202:5174"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
