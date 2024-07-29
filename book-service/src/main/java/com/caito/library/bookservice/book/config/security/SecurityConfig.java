package com.caito.library.bookservice.book.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/swagger-ui/**", "/.well-known/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/authorized").permitAll()
                .anyRequest().authenticated())

             .csrf(AbstractHttpConfigurer::disable)
             .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .oauth2Login(login -> login.loginPage("/api/v1/library/oauth2/authorization/client-app"))
             .oauth2Client(Customizer.withDefaults())
             .oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
