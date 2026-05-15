package com.minitrello.backend.security;

import com.minitrello.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.StringJoiner;

@Component
@Slf4j
public class JwtProvider {

    // Lấy giá trị từ application.properties
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Value("${jwt.valid-duration}")
    private long validDuration;

    // Hàm tạo Access Token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) // Định danh user
                .setIssuedAt(new Date()) // Thời gian tạo
                .setExpiration(new Date(System.currentTimeMillis() + validDuration)) // Thời gian hết hạn
                .claim("token_version", user.getTokenVersion()) // Phiên bản token (dùng để kick out máy cũ)
                .claim("scope", buildScope(user)) // Quyền hạn
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // Ký tên Token
                .compact();
    }

    // Biến Set<String> roles thành chuỗi cách nhau bởi khoảng trắng
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            // Sửa lại: Lấy getName() từ object Role
            user.getRoles().forEach(role -> stringJoiner.add(role.getName()));
        }
        return stringJoiner.toString();
    }

    // Hàm phụ trợ lấy Key từ chuỗi cấu hình
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(signerKey.getBytes());
    }

    // Hàm 1: Kiểm tra tính hợp lệ của Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token không hợp lệ hoặc đã hết hạn: {}", e.getMessage());
            return false;
        }
    }

    // Hàm 2: Trích xuất toàn bộ thông tin (Claims) từ Payload của Token
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}