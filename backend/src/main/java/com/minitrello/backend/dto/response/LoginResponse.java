package com.minitrello.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private boolean authenticated;
    private String token; // Vẫn trả về token ở body để test Postman cho tiện
}
