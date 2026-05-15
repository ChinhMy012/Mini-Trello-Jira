package com.minitrello.backend.security;

import com.minitrello.backend.entity.User;
import com.minitrello.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Lấy Token từ Cookie
            String token = extractTokenFromCookie(request);

            // 2. Nếu có Token và Token hợp lệ (chưa hết hạn, đúng chữ ký)
            if (token != null && jwtProvider.validateToken(token)) {
                Claims claims = jwtProvider.getClaims(token);
                String username = claims.getSubject();
                Integer tokenVersion = claims.get("token_version", Integer.class);

                // 3. Truy vấn DB để lấy version mới nhất của User
                User user = userRepository.findByUsername(username).orElse(null);

                // 4. KIỂM TRA KICK-OUT: Nếu version trong Token < version trong DB -> Từ chối
                if (user != null && user.getTokenVersion().equals(tokenVersion)) {

                    // 5. Trích xuất quyền (Roles) từ chuỗi "scope"
                    String scope = claims.get("scope", String.class);
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                            .filter(role -> !role.isEmpty())
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // 6. Ghi nhận đăng nhập thành công vào SecurityContext của Spring
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("Xác thực thành công cho user: {}", username);
                } else {
                    log.warn("Token version không khớp hoặc User không tồn tại (Có thể đã bị kick ra).");
                }
            }
        } catch (Exception e) {
            log.error("Lỗi xác thực: {}", e.getMessage());
        }

        // Dù thế nào cũng phải cho request đi tiếp đến các filter khác (nếu pass bước trên thì đã có SecurityContext)
        filterChain.doFilter(request, response);
    }

    // Hàm phụ trợ: Lấy giá trị của cookie tên là "jwt"
    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
}