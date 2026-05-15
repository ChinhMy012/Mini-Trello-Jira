package com.minitrello.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF: Nếu không tắt cái này, 100% các request POST (như register) sẽ bị chặn lại và báo lỗi 403.
                .csrf(csrf -> csrf.disable())

                // 2. Cấu hình phân luồng API
                .authorizeHttpRequests(auth -> auth
                        // Cấp quyền đi qua tự do cho Register và Login
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        // Tất cả các API khác phải có thẻ (Token) mới cho vào
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}