package com.example.gyeol.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()); // REST 테스트 편하게
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/register").permitAll()
                .anyRequest().permitAll() // 지금은 전체 허용. 필요 시 인증 걸자
        );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
