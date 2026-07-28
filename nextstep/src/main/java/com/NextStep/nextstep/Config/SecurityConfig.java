package com.NextStep.nextstep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for local API updates
            .cors(Customizer.withDefaults()) // Connects to your existing CorsConfig
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // Allow all API requests
                .anyRequest().authenticated()
            );
            
        return http.build();
    }
}