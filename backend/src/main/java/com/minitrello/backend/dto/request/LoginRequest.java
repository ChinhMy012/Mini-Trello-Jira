package com.minitrello.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Tên người dùng không được để trống.")
    private String username;

    @NotBlank(message = "Mật khẩu người dùng không được để trống.")
    private String password;
}