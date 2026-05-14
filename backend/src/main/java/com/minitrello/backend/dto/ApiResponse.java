package com.minitrello.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Ẩn các field null khi trả về JSON
public class ApiResponse<T> {
    // Gán giá trị mặc định là 1000 ngay tại đây
    private int code = 1000;
    private String message;
    private T data;

    // Sửa lại hàm static để gán code đúng
    public static <T> ApiResponse<T> OK(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(1000); // Đảm bảo trả về 1000
        response.setMessage(message);
        return response;
    }

}