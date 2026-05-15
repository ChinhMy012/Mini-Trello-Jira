package com.minitrello.backend.controller;

import com.minitrello.backend.dto.request.LoginRequest;
import com.minitrello.backend.dto.request.UserCreationRequest;
import com.minitrello.backend.dto.response.ApiResponse;
import com.minitrello.backend.dto.response.LoginResponse;
import com.minitrello.backend.dto.response.UserResponse;
import com.minitrello.backend.service.UserService;
import com.minitrello.backend.util.CookieHelper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CookieHelper cookieHelper;

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserCreationRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response) {

        // Gọi service xử lý login
        LoginResponse loginResponse = userService.login(request);

        // Sử dụng Helper để đóng gói JWT vào Cookie (Sống trong 1 ngày)
        cookieHelper.addJwtCookie(response, loginResponse.getToken(), 86400);

        return ApiResponse.<LoginResponse>builder()
                .data(loginResponse)
                .build();
    }
    @GetMapping("/me")
    public ApiResponse<String> getMyProfile() {
        // API này đã bị khóa, yêu cầu phải có Token hợp lệ
        return ApiResponse.<String>builder()
                .data("Chúc mừng! Bạn đã vượt qua chốt chặn Security và truy cập được API nội bộ.")
                .build();
    }
}