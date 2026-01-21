package com.sensprj.leo.config;

import com.sensprj.leo.security.AuditAccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuditAccessDeniedHandler deniedHandler) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                // 401 statt Browser-Popup (wenn nicht eingeloggt)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler(deniedHandler) // 403 + JSON + Audit
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Teacher darf Assessment erstellen/ändern
                        .requestMatchers(HttpMethod.POST,  "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT,   "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/assessments/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE,"/api/assessments/**").hasRole("TEACHER")

                        // Rest: eingeloggt
                        .anyRequest().authenticated()
                )

                // Wichtig: wenn du JWT/Cookies nutzt, lass basic/form aus.
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

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
