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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuditAccessDeniedHandler deniedHandler) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.accessDeniedHandler(deniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().permitAll()
                        // Teacher darf Assessment erstellen/ändern
                        .requestMatchers(HttpMethod.POST,  "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT,   "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE,"/api/assessments/**").hasRole("TEACHER")

                        // alles andere: nur eingeloggt (sonst würdest du wieder alles offen lassen)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://13.53.169.202:5174"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}