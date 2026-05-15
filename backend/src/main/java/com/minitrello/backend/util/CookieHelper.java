package com.minitrello.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieHelper {

    /**
     * Tạo và đính kèm HttpOnly Cookie vào Response
     */
    public void addJwtCookie(HttpServletResponse response, String token, long durationInSeconds) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true); // Chống XSS
        cookie.setSecure(false);   // Để false cho localhost (HTTP)
        cookie.setPath("/");       // Có hiệu lực cho toàn bộ domain
        cookie.setMaxAge((int) durationInSeconds);

        response.addCookie(cookie);
    }

    /**
     * Hỗ trợ xóa Cookie (Dùng cho tính năng Logout sau này)
     */
    public void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
